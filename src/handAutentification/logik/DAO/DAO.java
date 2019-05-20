package handAutentification.logik.DAO;

import handAutentification.logik.domains.BiometricalData;

import java.sql.*;

public class DAO {
    private static DAO ourInstance;
    private static String user = "root";
    private static String password = "root";
    private static String url = "jdbc:mysql://localhost:3306/hand_detector?useSSL=false&serverTimezone=UTC";

    public static DAO getInstance() {
        if (ourInstance == null)
            ourInstance = new DAO();
        return ourInstance;
    }

    private DAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int addPersonToDB(String name, String surname, BiometricalData biometricalData) {
        int insertedId = -1;
        String personQuery = String.format("insert into person_name (name, surname)" +
                " values ('%s', '%s')", name, surname);
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);

            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            while (resultSet.next()) {
                insertedId = resultSet.getInt(1);
            }
            System.out.println(insertedId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (insertedId != -1) {
            int handId = addHandWidthAndHeight(biometricalData);
            if (handId != -1) {
                addHandToPerson(insertedId, handId);
            } else {
                System.out.println("mistake in add person to db DAO");
            }
        }
        return insertedId;
    }

    private void addHandToPerson(int personId, int handId) {
        String personQuery = String.format("insert into hand_person (hand_id, person_id)" +
                " values (%d, %d)", handId, personId);
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(personQuery);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Add Hand to Person mistake!");
            e.printStackTrace();
        }
    }

    private int addHandWidthAndHeight(BiometricalData biometricalData) {
        // return hand id in hand_main
        int handId = -1;
        int heightId = addHeightInfo(biometricalData);
        int widthId = addWidthInfo(biometricalData);

        //add hand
        if (heightId > 0 && widthId > 0) {
            String handMain = String.format("insert into hand_main (width_id, height_id)" +
                    " values (%d, %d)", widthId, heightId);
            try {
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement ps = connection.prepareStatement(handMain, Statement.RETURN_GENERATED_KEYS);
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                while (resultSet.next()) {
                    handId = resultSet.getInt(1);
                }
                System.out.println("Hand id: " + handId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Hand add mistake, in DAO");
        }
        return handId;
    }

    private int addWidthInfo(BiometricalData biometricalData) {
        int insertedId = -1;
        String addQuery = String.format("insert into width (big, fore, middle," +
                        " no_name, little) values " +
                        "(%d, %d, %d, %d, %d)", (int) biometricalData.getBigFingerWidth(),
                (int) biometricalData.getForeFingerWidth(), (int) biometricalData.getMiddleFingerWidth(),
                (int) biometricalData.getNoNameFingerWidth(), (int) biometricalData.getLittleFingerWidth());
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(addQuery, Statement.RETURN_GENERATED_KEYS);

            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            while (resultSet.next()) {
                insertedId = resultSet.getInt(1);
            }
            System.out.println("Width id: " + insertedId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedId;
    }

    private int addHeightInfo(BiometricalData biometricalData) {
        int insertedId = -1;
        String addQuery = String.format("insert into height (big, fore, middle," +
                        " no_name, little) values " +
                        "(%d, %d, %d, %d, %d)", (int) biometricalData.getBigFingerHeight(),
                (int) biometricalData.getForeFingerHeight(), (int) biometricalData.getMiddleFingerHeight(),
                (int) biometricalData.getNoNameFingerHeight(), (int) biometricalData.getLittleFingerHeight());
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(addQuery, Statement.RETURN_GENERATED_KEYS);

            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            while (resultSet.next()) {
                insertedId = resultSet.getInt(1);
            }
            System.out.println("Height id: " + insertedId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedId;
    }

    // только относительные размеры!!!!!!!!!
    public String findPerson(BiometricalData originalBiometricalData) {
        String result = "Cant find this guy";
        String query = String.format("SELECT " +
                "hand_main.id, width.big, width.fore, " +
                "width.middle, width.no_name, width.little, " +
                "height.big, height.fore, " +
                "height.middle, height.no_name, height.little " +
                "FROM hand_main JOIN width on hand_main.width_id = width.id " +
                "JOIN height on hand_main.height_id = height.id");

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                BiometricalData tmp =
                        new BiometricalData(
                                resultSet.getDouble("height.middle"),
                                resultSet.getDouble("width.middle"),
                                resultSet.getDouble("height.fore"),
                                resultSet.getDouble("width.fore"),
                                resultSet.getDouble("height.no_name"),
                                resultSet.getDouble("width.no_name"),
                                resultSet.getDouble("height.little"),
                                resultSet.getDouble("width.little"),
                                resultSet.getDouble("height.big"),
                                resultSet.getDouble("width.big"));
                if (tmp.equals(originalBiometricalData)){
                    System.out.println("We find person with hand id: "
                            + resultSet.getString("hand_main.id"));
                    result = "Hand id = " + resultSet.getString("hand_main.id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }
}
