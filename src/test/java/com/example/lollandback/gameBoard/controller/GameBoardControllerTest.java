package com.example.lollandback.gameBoard.controller;

import com.example.lollandback.gameBoard.controller.GameBoardController;
import com.example.lollandback.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import com.example.lollandback.gameBoard.domain.GameBoard;
import com.example.lollandback.gameBoard.service.GameBoardService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GameBoardControllerTest {

    @Test
    void update() throws IOException {
        // given
        GameBoardService gameBoardServiceMock = mock(GameBoardService.class);
        GameBoard gameBoardMock = mock(GameBoard.class);
        MultipartFile[] files = {new MockMultipartFile("file", "hello.txt", "text/plain", "Hello, World!".getBytes())};
        List<Integer> removeFileIds = Arrays.asList(1, 2, 3);
        GameBoardController gameBoardController = new GameBoardController(gameBoardServiceMock);

        when(gameBoardServiceMock.validate(gameBoardMock)).thenReturn(true);
        when(gameBoardServiceMock.update(gameBoardMock, removeFileIds, files)).thenReturn(true);

        // when
        ResponseEntity responseEntity = gameBoardController.edit(gameBoardMock, removeFileIds, files);

        // then
        assertEquals(ResponseEntity.ok().build(), responseEntity);

        // Verify that validate and update methods were called with correct parameters
        verify(gameBoardServiceMock).validate(gameBoardMock);
        verify(gameBoardServiceMock).update(gameBoardMock, removeFileIds, files);
    }

    @Test
    void delete() {
        // given
        GameBoardService gameBoardServiceMock = mock(GameBoardService.class);
        GameBoardController gameBoardController = new GameBoardController(gameBoardServiceMock);
        Long id = 1L;

        when(gameBoardServiceMock.delete(id)).thenReturn(true);

        // when
        ResponseEntity responseEntity = gameBoardController.delete(id);

        // then
        assertEquals(ResponseEntity.ok().build(), responseEntity);

        // Verify that delete method was called with correct parameter
        verify(gameBoardServiceMock).delete(id);
    }

    @Test
    void add() throws IOException {

        //add 테스트 코드 순서 설명 : given -> when -> then -> verify
        // given : 테스트를 위한 객체 생성
        // given 순서 : GameBoardService 객체 생성 -> GameBoard 객체 생성 -> MultipartFile 객체 생성 -> Member 객체 생성 -> GameBoardController 객체 생성
        // when : 테스트할 메소드 호출
        // when 순서 : gameBoardServiceMock.validate 메소드 호출 -> gameBoardServiceMock.save 메소드 호출 -> gameBoardController.add 메소드 호출
        // then : 테스트 결과 검증
        // then 순서 : ResponseEntity 객체 생성 -> assertEquals 메소드 호출
        // verify : 메소드 호출 여부 검증
        // verify 순서 : gameBoardServiceMock.validate 메소드 호출 여부 검증 -> gameBoardServiceMock.save 메소드 호출 여부 검증



        // given
        GameBoardService gameBoardServiceMock = mock(GameBoardService.class);
        // 설명 : GameBoardService 객체를 mock으로 생성 후 gameBoardServiceMock 변수에 할당

        // mock : 가짜 객체를 생성하는 메소드
        GameBoard gameBoardMock = mock(GameBoard.class);
        // 설명 : GameBoard 객체를 mock으로 생성 후 gameBoardMock 변수에 할당
        // 이유 : GameBoard 객체를 생성하면 실제 데이터베이스에 접근하게 되므로 mock 객체를 생성하여 테스트
        //  게임보드 서비스와 게임보드 객체 둘 다 생성하는 이유 : 게임보드 서비스의 메소드를 호출할 때 게임보드 객체를 인자로 전달하기 때문
        MultipartFile[] files = {new MockMultipartFile("file", "hello.txt", "text/plain", "Hello, World!".getBytes())};
        // 설명 : MultipartFile 객체를 mock으로 생성 후 files 변수에 할당
        // 이유 : MultipartFile 객체를 생성하면 실제 파일을 업로드하게 되므로 mock 객체를 생성하여 테스트


        Member login = mock(Member.class);

        GameBoardController gameBoardController = new GameBoardController(gameBoardServiceMock);

        when(gameBoardServiceMock.validate(gameBoardMock)).thenReturn(true);
        when(gameBoardServiceMock.save(gameBoardMock, files, login)).thenReturn(true);

        // when
        ResponseEntity responseEntity = gameBoardController.add(gameBoardMock, files, login);

        // then
        assertEquals(ResponseEntity.ok().build(), responseEntity);

        // Verify that validate and save methods were called with correct parameters and correct number of times
        verify(gameBoardServiceMock, times(1)).validate(gameBoardMock);
        //설명 : gameBoardServiceMock.validate 메소드가 gameBoardMock 객체를 인자로 호출되었는지 검증
        // verify 메서드는 메소드 호출 여부를 검증하는 메소드이며 times 메소드는 메소드 호출 횟수를 검증하는 메소드
        // validate의 역할 : 게임보드 객체의 유효성을 검증하는 메소드이며 파라메터는 게임보드 객체이다.
        // 이유는 게임보드 객체의 유효성을 검증하기 위해 게임보드 객체를 인자로 전달해야 하기 때문
        verify(gameBoardServiceMock, times(1)).save(gameBoardMock, files, login);
    }
}