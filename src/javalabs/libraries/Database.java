package javalabs.libraries;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import javafx.scene.image.*;

public class Database {
    private String url = "jdbc:mysql://localhost:3306/javalabs?autoReconnect=true&useSSL=false&characterEncoding=utf-8";
    private String user = "root";
    private String password = "2883455";
    private static Connection connection;
    private static Statement statement;

    public List<Object[]> query(String queryString) {
        List<Object[]> resultList = new ArrayList<Object[]>();
        try {
            // Подключение
            connection = DriverManager.getConnection(url, user, password);
            // Создание запроса
            statement = connection.createStatement();
            // Запрос
            ResultSet result = statement.executeQuery(queryString);
            // Получение метаописания результата запроса
            ResultSetMetaData rdsc = result.getMetaData();
            // Количество колонок
            int colCount = rdsc.getColumnCount();
            // Названия колонок
            String[] columnNames = new String[colCount];
            // Типы колонок
            String[] columnTypes = new String[colCount];
            for(int i = 0; i < colCount; i++){
                columnNames[i] = rdsc.getColumnName(i+1);
                columnTypes[i] = rdsc.getColumnTypeName(i+1);
            }
            while(result.next()){
                // Содержимое колонок
                Object[] fieldsArray = new Object[colCount];
                // Перебор объекта запроса
                for(int i = 0; i < colCount; i++){
                    switch(columnTypes[i]){
                        case "INT": // Для int'ов
                            fieldsArray[i] = result.getInt(columnNames[i]);
                            break;
                        case "MEDIUMBLOB": // Для фоток
                            Blob imageBlob = result.getBlob(columnNames[i]);
                            InputStream is = imageBlob.getBinaryStream();
                            Image image = new Image(is, 80, 0, true, true);
                            ImageView render = new ImageView();
                            render.setImage(image);
                            fieldsArray[i] = render;
                            break;
                        default: // По-умолчанию всё строки
                            fieldsArray[i] = result.getString(columnNames[i]);
                    }
                }
                resultList.add(fieldsArray);
            }
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultList;
    }

    public int update(String queryString){
        try {
            // Подключение
            connection = DriverManager.getConnection(url, user, password);
            // Создание запроса
            statement = connection.createStatement();
            // Выполнение запроса
            statement.executeUpdate(queryString);
            // Закрытие соединения
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public Connection unsafeGetConnection() throws Exception{
        return DriverManager.getConnection(url, user, password);
    }

}
