package task6.lesson2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.suveren.task6.lesson2.controller.DepartmentController;
import ru.suveren.task6.lesson2.model.Department;
import ru.suveren.task6.lesson2.service.DepartmentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Department testDepartment;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
        testDepartment = new Department(1L, "IT Department");
    }

    @Test
    void save_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
        doThrow(new RuntimeException("Database error")).when(departmentService).save(any(Department.class));

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDepartment)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("error in server"));

        verify(departmentService, times(1)).save(any(Department.class));
    }

    @Test
    void get_ShouldReturnDepartment_WhenDepartmentExists() throws Exception {
        when(departmentService.get(1L)).thenReturn(testDepartment);

        mockMvc.perform(get("/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("IT Department"));

        verify(departmentService, times(1)).get(1L);
    }

    @Test
    void get_ShouldReturnNotFound_WhenDepartmentDoesNotExist() throws Exception {
        when(departmentService.get(999L)).thenReturn(null);

        mockMvc.perform(get("/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(departmentService, times(1)).get(999L);
    }

    @Test
    void get_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
        when(departmentService.get(1L)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/{id}", 1L))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("error in server"));

        verify(departmentService, times(1)).get(1L);
    }

    @Test
    void update_ShouldReturnOk_WhenDepartmentUpdatedSuccessfully() throws Exception {
        Department updatedDepartment = new Department(1L, "HR Department");
        when(departmentService.update(any(Department.class))).thenReturn(true);

        mockMvc.perform(put("/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDepartment)))
                .andExpect(status().isOk())
                .andExpect(content().string("department update"));

        verify(departmentService, times(1)).update(any(Department.class));
    }

    @Test
    void update_ShouldReturnNotFound_WhenDepartmentDoesNotExist() throws Exception {
        Department updatedDepartment = new Department(999L, "HR Department");
        when(departmentService.update(any(Department.class))).thenReturn(false);

        mockMvc.perform(put("/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDepartment)))
                .andExpect(status().isNotFound());

        verify(departmentService, times(1)).update(any(Department.class));
    }

    @Test
    void update_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
        when(departmentService.update(any(Department.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDepartment)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("error in server"));

        verify(departmentService, times(1)).update(any(Department.class));
    }

    @Test
    void delete_ShouldReturnOk_WhenDepartmentDeletedSuccessfully() throws Exception {
        doNothing().when(departmentService).delete(any(Department.class));

        mockMvc.perform(delete("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDepartment)))
                .andExpect(status().isOk())
                .andExpect(content().string("department delete"));

        verify(departmentService, times(1)).delete(any(Department.class));
    }

    @Test
    void delete_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
        doThrow(new RuntimeException("Database error")).when(departmentService).delete(any(Department.class));

        mockMvc.perform(delete("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDepartment)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));

        verify(departmentService, times(1)).delete(any(Department.class));
    }
}