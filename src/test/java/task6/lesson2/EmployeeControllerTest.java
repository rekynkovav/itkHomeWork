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
import ru.suveren.task6.lesson2.controller.EmployeeController;
import ru.suveren.task6.lesson2.model.Department;
import ru.suveren.task6.lesson2.model.Employee;
import ru.suveren.task6.lesson2.service.EmployeeService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Employee testEmployee;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        testDepartment = new Department(1L, "IT Department");
        testEmployee = new Employee(1L, "John", "Doe", "Developer", 50000, testDepartment);
    }

    @Test
    void save_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
        doThrow(new RuntimeException("Database error")).when(employeeService).save(any(Employee.class));

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEmployee)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("error in server"));

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    void get_ShouldReturnEmployee_WhenEmployeeExists() throws Exception {
        when(employeeService.get(1L)).thenReturn(testEmployee);

        mockMvc.perform(get("/employee/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.position").value("Developer"))
                .andExpect(jsonPath("$.salary").value(50000))
                .andExpect(jsonPath("$.department.id").value(1L))
                .andExpect(jsonPath("$.department.name").value("IT Department"));

        verify(employeeService, times(1)).get(1L);
    }

    @Test
    void get_ShouldReturnNotFound_WhenEmployeeDoesNotExist() throws Exception {
        when(employeeService.get(999L)).thenReturn(null);

        mockMvc.perform(get("/employee/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).get(999L);
    }

    @Test
    void get_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
        when(employeeService.get(1L)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/employee/{id}", 1L))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("error in server"));

        verify(employeeService, times(1)).get(1L);
    }

    @Test
    void update_ShouldReturnOk_WhenEmployeeUpdatedSuccessfully() throws Exception {
        Employee updatedEmployee = new Employee(1L, "Jane", "Smith", "Senior Developer", 70000, testDepartment);
        when(employeeService.update(any(Employee.class))).thenReturn(true);

        mockMvc.perform(put("/employee/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(content().string("employee update"));

        verify(employeeService, times(1)).update(any(Employee.class));
    }

    @Test
    void update_ShouldReturnNotFound_WhenEmployeeDoesNotExist() throws Exception {
        Employee updatedEmployee = new Employee(999L, "Jane", "Smith", "Senior Developer", 70000, testDepartment);
        when(employeeService.update(any(Employee.class))).thenReturn(false);

        mockMvc.perform(put("/employee/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).update(any(Employee.class));
    }

    @Test
    void update_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
        when(employeeService.update(any(Employee.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/employee/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEmployee)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("error in server"));

        verify(employeeService, times(1)).update(any(Employee.class));
    }

    @Test
    void delete_ShouldReturnOk_WhenEmployeeDeletedSuccessfully() throws Exception {
        doNothing().when(employeeService).delete(any(Employee.class));

        mockMvc.perform(delete("/employee", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEmployee)))
                .andExpect(status().isOk())
                .andExpect(content().string("employee delete"));

        verify(employeeService, times(1)).delete(any(Employee.class));
    }

    @Test
    void delete_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
        doThrow(new RuntimeException("Database error")).when(employeeService).delete(any(Employee.class));

        mockMvc.perform(delete("/employee", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEmployee)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));

        verify(employeeService, times(1)).delete(any(Employee.class));
    }
}