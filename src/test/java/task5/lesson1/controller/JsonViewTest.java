package task5.lesson1.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.suveren.task5.lesson1.model.Order;
import ru.suveren.task5.lesson1.model.User;
import ru.suveren.task5.lesson1.model.Views;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonViewTest {

    ObjectMapper mapper = new ObjectMapper();
    User user = new User(1L, "John", "password", "john@gmail.com", List.of(new Order(), new Order()));

    @Test
    void testUserSummary() throws Exception {

        String json = mapper
                .writerWithView(Views.UserSummary.class)
                .writeValueAsString(user);

        assertThat(json).contains("John");
        assertThat(json).doesNotContain("password");
    }

    @Test
    void testUserDetails() throws Exception {
        String json = mapper
                .writerWithView(Views.UserDetails.class)
                .writeValueAsString(user);

        JsonNode node = mapper.readTree(json);

        assertThat(node.get("name").asText()).isEqualTo("John");
        assertThat(node.get("email").asText()).isEqualTo("john@gmail.com");
        assertThat(node.has("password")).isFalse();
        assertThat(node.has("orderList")).isTrue();
    }

}
