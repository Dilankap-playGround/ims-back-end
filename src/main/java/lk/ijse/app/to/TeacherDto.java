package lk.ijse.app.to;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto implements Serializable {
    @Null
    private  Integer id;
    @NotBlank(message = "name can't be empty")
    @Pattern(regexp = "/^[A-za-z ]+$/",message = "Invalid NAme")
    private String name;
    @NotBlank(message = "Contact can't be empty")
    @Pattern(regexp = "/\\d{3}-\\d{7}/",message = "Invalid Number")
    private String contact;
}
