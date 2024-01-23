package com.example.lollandback.gameBoard.service;


import com.example.lollandback.gameBoard.domain.BoardWriter;
import com.example.lollandback.gameBoard.domain.GameBoard;
import com.example.lollandback.gameBoard.domain.GameBoardFile;
import com.example.lollandback.gameBoard.mapper.BoardMapper;
import com.example.lollandback.gameBoard.mapper.CommentMapper;
import com.example.lollandback.gameBoard.mapper.FileMapper;
import com.example.lollandback.gameBoard.mapper.LikeMapper;
import com.example.lollandback.member.domain.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameBoardService {

    private final BoardMapper mapper;
    private final FileMapper fileMapper;
    private final LikeMapper likeMapper;
    private final CommentMapper commentMapper;

    private final HttpSession session;
    private final HttpServletResponse response;
    private final HttpServletRequest request;





    private final S3Client s3;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    @Value("${image.file.prefix}")
    private String urlPrefix;


    public boolean save(GameBoard gameBoard, MultipartFile[] files, @SessionAttribute(value="login",required = false)Member login) throws IOException {

        gameBoard.setMember_id(login.getMember_login_id());
        int cnt = mapper.insert(gameBoard);

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String file_url = urlPrefix + upload(gameBoard.getId(), files[i]);
                fileMapper.insert(gameBoard.getId(), files[i].getOriginalFilename(),file_url);

            }
        }
        return cnt == 1;
    }

    private String upload(Long gameboardId, MultipartFile file) throws IOException {

        String key = "lolland/gameboard/" + gameboardId + "/" + file.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return key;
    }

    public boolean validate(GameBoard gameBoard) {
        if (gameBoard == null) {
            System.out.println("board null");
            return false;
        }
        if (gameBoard.getBoard_content() == null || gameBoard.getBoard_content().isBlank()) {
            System.out.println("content error");
            return false;
        }

        if (gameBoard.getTitle() == null || gameBoard.getTitle().isBlank()) {
            System.out.println("title error");
            return false;
        }
        return true;

    }

    public Map<String, Object> list(Integer page, String keyword, String category,String sortBy) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        int countAll = mapper.countAll("%" + keyword + "%",category);
        int lastPageNumber = (countAll - 1) / 10 + 1;
        int startPageNumber = (page - 1) / 10 * 10 + 1;
        int endPageNumber = startPageNumber + 9;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);
        int prevPageNumber = startPageNumber - 10;
        int nextPageNumber = endPageNumber + 1;

        pageInfo.put("currentPageNumber", page);
        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);
        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if (nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        int from = (page - 1) * 10;
        map.put("gameBoardList", mapper.selectAll(from, "%" + keyword + "%",category,sortBy));
        map.put("pageInfo", pageInfo);

        return map;
    }

    public GameBoard get(Integer id) {

        String cookieValue = getCookieValue("board_" + id);
        if (cookieValue == null) {
            boardCount(id);
            addCookie("board_"+id,"viewed");
        }


//      게시물, 파일 조회 부분
        GameBoard gameBoard = mapper.selectById(id);
        List<GameBoardFile> boardFiles = fileMapper.selectNamesBygameboardId(id);
        gameBoard.setFiles(boardFiles);

        return gameBoard;
    }

    public String getCookieValue(String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    //쿠키 생성 및 응답 추가 : 맥스 에이지는 24시간으로 설정
    public void addCookie(String name, String value){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }



    public void boardCount(Integer id) {
        mapper.boardCount(id);

    }



    public boolean update(GameBoard gameBoard, List<Integer> removeFileIds, MultipartFile[] uploadFiles) throws IOException{
        if (removeFileIds != null) {
            for (Integer id : removeFileIds) {
                GameBoardFile file = fileMapper.selectById(id);
                String key = "lolland/gameboard/" + id + "/" + file.getFile_name();
                DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                        .bucket(bucket).key(key).build();

                s3.deleteObject(objectRequest);
                fileMapper.deleteById(id);
            }
        }
        if (uploadFiles != null) {
            for (MultipartFile file : uploadFiles) {
                upload(gameBoard.getId(), file);
                String file_url = urlPrefix + upload(gameBoard.getId(), file);
                fileMapper.insert(gameBoard.getId(), file.getOriginalFilename(),file_url);

            }
        }
        return mapper.update(gameBoard) == 1;
    }

    public boolean delete(Integer id) {

        commentMapper.deleteByBoardId(id);
        likeMapper.deleteByBoardId(id);

        deleteFile(id);

        return mapper.deleteById(id) == 1;
    }

    private void deleteFile(Integer id) {
        List<GameBoardFile> boardFiles = fileMapper.selectNamesBygameboardId(id);

        for (GameBoardFile file : boardFiles) {

            String key = "lolland/gameboard/" + id + "/" + file.getFile_name();

            DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            s3.deleteObject(objectRequest);
        }
        fileMapper.deleteByBoard(id);
    }



    public List<GameBoard> notice() {
        return mapper.selectNotice();
    }

    public List<GameBoard> top() {
        return mapper.selectTop();
    }

    public List<GameBoard> today(){
        return mapper.selectToday();}

    public List<GameBoard> writtenPost(String writer) {

        return mapper.selectByMemberId(writer);
    }

    public BoardWriter postMemberInfo(String writer) {
        return mapper.selectMemberById(writer);

    }
}
