package lk.ijse.app.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.app.to.TeacherDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/teachers")
public class TeacherHttpController {
    private final HikariDataSource pool;
    public TeacherHttpController(){
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
    @PostMapping(consumes = "application/json" , produces = "application/json")
    public TeacherDto createTeacher(@RequestBody @Validated TeacherDto teacherDto){
        String name = teacherDto.getName();
        String contact = teacherDto.getContact();
        System.out.println(teacherDto.getId());
        try(Connection connection = pool.getConnection()){
            PreparedStatement stm = connection.prepareStatement("INSERT INTO teacher ( name, contact) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1,name);
            stm.setString(2,contact);
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            generatedKeys.next();
            int generatedId= generatedKeys.getInt(1);
            teacherDto.setId(generatedId);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        System.out.println("create Teacher()");
        System.out.println(teacherDto);
        return teacherDto;
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{id}")
    public void updateTeacher(@PathVariable int id ,@RequestBody @Validated TeacherDto teacherDto){

        try (Connection connection = pool.getConnection()){
            PreparedStatement existstm = connection.prepareStatement("SELECT * FROM teacher WHERE id=?");
            existstm.setInt(1,id);
            ResultSet resultSet = existstm.executeQuery();
            if(!resultSet.next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher Not Found");
            }

            PreparedStatement pstm = connection.prepareStatement("UPDATE teacher SET name= ? ,contact=? WHERE id =?");
            pstm.setString(1,teacherDto.getName());
            pstm.setString(2,teacherDto.getContact());
            pstm.setInt(3,id);
            int i = pstm.executeUpdate();


        }catch(SQLException e){
            throw  new RuntimeException(e);
        }
        System.out.println("update Teacher" +id);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteTeacher(@PathVariable int id){
        System.out.println("deleteTeacher()" + id);
    try (Connection connection = pool.getConnection()){
        PreparedStatement existstm = connection.prepareStatement("SELECT * FROM teacher WHERE id=?");
        existstm.setInt(1,id);
        ResultSet resultSet = existstm.executeQuery();
        if(!resultSet.next()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher Not Found");
        }
        int teacherId =resultSet.getInt(1);

        PreparedStatement pstm = connection.prepareStatement("DELETE FROM teacher WHERE id=?");
       pstm.setInt(1,id);

    }catch(SQLException e){
        throw  new RuntimeException(e);
    }
    }

    @GetMapping(produces = "application/json")
    public List<TeacherDto> getAllTeachers(){
        try (Connection connection = pool.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM teacher");
            List<TeacherDto> teacherDtos = new ArrayList<>();
            while (resultSet.next()){
                int anInt = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String contact = resultSet.getString("contact");
                TeacherDto teacherDto = new TeacherDto(anInt, name, contact);
                teacherDtos.add(teacherDto);
            }
            return teacherDtos;

        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public  TeacherDto getTeacherById(@PathVariable int id){
        try (Connection connection = pool.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM teacher WHERE id = ?");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Teacher Not Found");
            int anInt = resultSet.getInt(1);
            String string = resultSet.getString(2);
            String string1 = resultSet.getString(3);
            TeacherDto teacherDto = new TeacherDto(anInt, string, string1);
            return teacherDto;

        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

}


