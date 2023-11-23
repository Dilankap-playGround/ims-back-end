package lk.ijse.app.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.app.to.CourseDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/courses")
public class coursesHttpController {
    private final HikariDataSource pool;
    public coursesHttpController(){
        System.out.println("im here");
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/dep11_ims");
        config.setUsername("root");
        config.setPassword("0716409353dD.");
        config.addDataSourceProperty("maximumPoolSize", 20);
        pool= new HikariDataSource(config);
    }

    @PreDestroy
    public void destroy(){
        pool.close();
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public CourseDto createCourse(@RequestBody @Validated CourseDto course) {
        String name = course.getName();
        int duration = course.getDurationInMonths();
        try(Connection connection = pool.getConnection()){
            PreparedStatement stm = connection.prepareStatement("INSERT INTO course ( name, duration_in_months) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1,name);
            stm.setInt(2,duration);
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            generatedKeys.next();
            int generatedId= generatedKeys.getInt(1);
            course.setId(generatedId);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        System.out.println(course);
        return course;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{id}", consumes = "application/json")
    public void updateCourse(@PathVariable int id, @RequestBody @Validated CourseDto course) {
        System.out.println("updateCourse()");
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id) {
        System.out.println("deleteCourse()");
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public CourseDto getCourse(@PathVariable int id) {
        System.out.println("getCourse()");
        return null;
    }

    @GetMapping(produces = "application/json")
    public List<CourseDto> getAllCourses() {
        System.out.println("getAllCourses()");
        return null;
    }
}
