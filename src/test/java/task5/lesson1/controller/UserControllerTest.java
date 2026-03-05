package task5.lesson1.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import ru.suveren.task5.lesson1.controller.UserController;
import ru.suveren.task5.lesson1.dto.request.UserCreateRequest;
import ru.suveren.task5.lesson1.dto.request.UserUpdateRequest;
import ru.suveren.task5.lesson1.model.Order;
import ru.suveren.task5.lesson1.model.User;
import ru.suveren.task5.lesson1.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John", "password", "john@gmail.com", List.of(new Order(), new Order()));
    }

    @Test
    void testGetListUsers() throws Exception {
        when(userService.findAll()).thenReturn(List.of(user));
        ResponseEntity<List<User>> response = userController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("John");

        verify(userService).findAll();
    }

    @Test
    void testGetUsersById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUser(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("John");

        verify(userService).getUserById(1L);
    }

    @Test
    void testGetUsersById_NotFound404() throws Exception {
        when(userService.getUserById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userController.getUser(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);

        verify(userService).getUserById(999L);
    }

    @Test
    void testCreatUser() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setName("John");

        User mappedUser = new User();
        mappedUser.setName("John");

        User savadUser = new User();
        savadUser.setId(1L);
        savadUser.setName("John");

        when(modelMapper.map(request, User.class)).thenReturn(mappedUser);
        when(userService.save(mappedUser)).thenReturn(savadUser);

        User result = userController.createUser(request);

        assertThat(result.getId()).isEqualTo(1L);
        verify(modelMapper).map(request, User.class);
        verify(userService).save(mappedUser);
    }

    @Test
    void testUpdateUser() {

        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("new@mail.com");

        User mappedUser = new User();
        mappedUser.setEmail("new@mail.com");

        when(modelMapper.map(request, User.class)).thenReturn(mappedUser);

        userController.updateUser(1L, request);

        assertThat(mappedUser.getId()).isEqualTo(1L);
        verify(modelMapper).map(request, User.class);
        verify(userService).upDateUserFields(1L, "new@mail.com");
    }

    @Test
    void testDeleteUserById() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        userController.deleteUserById(1L);

        verify(userService).getUserById(1L);
        verify(userService).deleteById(1L);
    }

    @Test
    void testDeleteUserById_notFound404() {
        when(userService.getUserById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userController.deleteUserById(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);

        verify(userService).getUserById(999L);
        verify(userService, never()).deleteById(any());
    }
}
