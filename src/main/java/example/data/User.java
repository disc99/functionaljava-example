package example.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Sample User Bean
 */
@AllArgsConstructor
@Data
public class User {
    public int age;
    public String name;
    public String email;
}