import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class Main {

    private static final String CONNECTION_STRING =
            "jdbc:mysql://localhost:3306/minions_db";
    private static final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));
    private static Connection connection;

    public static void main(String[] args) throws IOException, SQLException {

        connection = getConnection();

        System.out.println("Enter Exercise number:");
        int exNum = Integer.parseInt(reader.readLine());

        switch (exNum) {
            case 2 -> getVillainsWithMoreThan15Minions();
            case 3 -> getVillainWithAllMinions();
            case 4 -> addMinionToDatabase();
            case 5 -> changeTownNamesToUpperCase();
            case 6 -> removeVillain();
            case 7 -> printAllMinionNames();
            case 8 -> increaseMinionsAge();
            case 9 -> increaseAgeUsingStoredProcedure();
        }


    }

    private static void getVillainsWithMoreThan15Minions() throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "SELECT name, COUNT(DISTINCT mv.minion_id) AS 'count' " +
                        "FROM villains " +
                        "JOIN minions_villains mv on villains.id = mv.villain_id " +
                        "GROUP BY mv.villain_id " +
                        "HAVING count > 15 " +
                        "ORDER BY count DESC;"
        );

        ResultSet rs = ps.executeQuery();

        StringBuilder result = new StringBuilder();
        while (rs.next()) {
            result.append(
                    String.format("%s %d",
                            rs.getString("name"),
                            rs.getInt("count")))
                    .append("\n");
        }

        System.out.println(result.toString().trim());
    }

    private static void getVillainWithAllMinions() throws IOException, SQLException {
        System.out.println("Enter Villain ID:");
        int id = Integer.parseInt(reader.readLine());

        String villainName = findEntityNameById("villains", id);

        if (villainName == null) {
            System.out.printf("No villain with ID %d exists in the database.", id);
            return;
        }

        String minions = getMinionsByVillainId(id);

        System.out.println("Villain: " + villainName);
        System.out.println(minions);
    }

    private static String findEntityNameById(String tableName, int id) throws SQLException {
        String query = String.format("SELECT name FROM %s WHERE id = ?;",
                tableName);
        PreparedStatement ps =
                connection.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("name");
        }
        return null;
    }

    private static String getMinionsByVillainId(int id) throws SQLException {
        StringBuilder minions = new StringBuilder();

        PreparedStatement ps =
                connection.prepareStatement(
                        "SELECT name, age " +
                                "FROM minions " +
                                "JOIN minions_villains mv on minions.id = mv.minion_id " +
                                "WHERE villain_id = ?;"
                );
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        int index = 0;
        while (rs.next()) {
            minions.append(
                    String.format("%d. %s %d",
                            ++index,
                            rs.getString("name"),
                            rs.getInt("age")
                    )).append("\n");
        }

        return minions.toString().trim();
    }

    private static void addMinionToDatabase() throws IOException, SQLException {
        System.out.println("Enter minion info:");
        String[] minionInfo = reader.readLine().split("\\s+");

        System.out.println("Enter villain info:");
        String[] villainInfo = reader.readLine().split("\\s+");

        String minionName = minionInfo[1];
        int minionAge = Integer.parseInt(minionInfo[2]);
        String townName = minionInfo[3];
        String villainName = villainInfo[1];

        if (findEntityByName("towns", townName) == null) {
            System.out.println(addTown(townName));
        }

        if (findEntityByName("villains", villainName) == null) {
            System.out.println(addVillain(villainName));
        }

        System.out.println(addMinion(minionName, minionAge, villainName));
    }

    private static String findEntityByName(String tableName, String name) throws SQLException {
        String query = String.format("SELECT name FROM %s WHERE name = ?",
                tableName);
        PreparedStatement ps =
                connection.prepareStatement(query);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("name");
        }
        return null;
    }

    private static String addTown(String townName) throws SQLException {
        PreparedStatement ps =
                connection.prepareStatement(
                        "INSERT INTO towns (name) " +
                                "VALUES (?);"
                );
        ps.setString(1, townName);
        ps.executeUpdate();

        return String.format("Town %s was added to the database.",
                townName);
    }

    private static String addVillain(String villainName) throws SQLException {
        PreparedStatement ps =
                connection.prepareStatement(
                        "INSERT INTO villains (name, evilness_factor) " +
                                "VALUES (?, 'evil');"
                );
        ps.setString(1, villainName);
        ps.executeUpdate();

        return String.format("Villain %s was added to the database.",
                villainName);
    }

    private static String addMinion(String minionName, int minionAge, String villainName) throws SQLException {
        PreparedStatement psMinions =
                connection.prepareStatement(
                        "INSERT INTO minions (name, age) " +
                                "VALUES (?, ?);"
                );
        psMinions.setString(1, minionName);
        psMinions.setInt(2, minionAge);
        psMinions.executeUpdate();

        int minionId = findIdByName("minions", minionName);
        int villainId = findIdByName("villains", villainName);

        PreparedStatement psMinionsVillains =
                connection.prepareStatement(
                        "INSERT INTO minions_villains (minion_id, villain_id) " +
                                "VALUES (?, ?);"
                );
        psMinionsVillains.setInt(1, minionId);
        psMinionsVillains.setInt(2, villainId);
        psMinionsVillains.executeUpdate();

        return String.format("Successfully added %s to be minion of %s.",
                minionName, villainName);
    }

    private static int findIdByName(String tableName, String name) throws SQLException {
        String query = String.format("SELECT id FROM %s WHERE name = ?",
                tableName);
        PreparedStatement ps =
                connection.prepareStatement(query);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
        return 0;
    }

    private static void changeTownNamesToUpperCase() throws SQLException, IOException {
        System.out.println("Enter country name:");
        String countryName = reader.readLine();

        int changedTowns = makeTownNamesToUpperCase(countryName);

        if (changedTowns != 0) {
            List<String> result = findTownNamesByCountry(countryName);
            System.out.printf("%d town names were affected.%n", changedTowns);
            System.out.println(result);
        } else {
            System.out.println("No town names were affected.");
        }
    }

    private static int makeTownNamesToUpperCase(String countryName) throws SQLException {
        PreparedStatement psUpdate =
                connection.prepareStatement(
                        "UPDATE towns " +
                                "SET name = UPPER(name) " +
                                "WHERE country = ?;"
                );
        psUpdate.setString(1, countryName);
        return psUpdate.executeUpdate();
    }

    private static List<String> findTownNamesByCountry(String countryName) throws SQLException {
        PreparedStatement psSelect =
                connection.prepareStatement(
                        "SELECT name FROM towns " +
                                "WHERE country = ?;"
                );
        psSelect.setString(1, countryName);
        ResultSet rs = psSelect.executeQuery();

        List<String> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rs.getString("name"));
        }
        return result;
    }

    private static void removeVillain() throws IOException, SQLException {
        System.out.println("Enter Villain ID:");
        int villainId = Integer.parseInt(reader.readLine());

        String villainName = findEntityNameById("villains", villainId);

        if (villainName == null) {
            System.out.println("No such villain was found");
            return;
        }

        int releasedMinionsCount = releaseMinionsByVillainId(villainId);
        deleteVillain(villainId);

        System.out.printf("%s was deleted%n%d minions released%n",
                villainName,
                releasedMinionsCount);
    }

    private static int releaseMinionsByVillainId(int villainId) throws SQLException {
        PreparedStatement ps =
                connection.prepareStatement(
                        "DELETE FROM minions_villains " +
                                "WHERE villain_id = ?;"
                );
        ps.setInt(1, villainId);

        return ps.executeUpdate();
    }

    private static void deleteVillain(int villainId) throws SQLException {
        PreparedStatement ps =
                connection.prepareStatement(
                        "DELETE FROM villains " +
                                "WHERE id = ?;"
                );
        ps.setInt(1, villainId);
        ps.executeUpdate();
    }

    private static void printAllMinionNames() throws SQLException {
        Deque<String> minions = new ArrayDeque<>();

        PreparedStatement ps =
                connection.prepareStatement(
                        "SELECT name FROM minions;"
                );

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            minions.add(rs.getString("name"));
        }

        StringBuilder result = new StringBuilder();
        while (!minions.isEmpty()) {
            result.append(minions.removeFirst()).append("\n");
            result.append(minions.removeLast()).append("\n");
        }

        System.out.println(result.toString().trim());
    }

    private static void increaseMinionsAge() throws IOException, SQLException {
        System.out.println("Enter minion IDs to increase their age:");
        int[] ids = Arrays.stream(reader.readLine().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();

        for (int id : ids) {
            increaseMinionsAgeById(id);
            makeNameStartWithLowerCase(id);
        }

        printALlMinionsNameAndAge();
    }

    private static void increaseMinionsAgeById(int id) throws SQLException {
        PreparedStatement ps =
                connection.prepareStatement(
                        "UPDATE minions " +
                                "SET age = age + 1 " +
                                "WHERE id = ?;"
                );
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    private static void makeNameStartWithLowerCase(int id) throws SQLException {
        PreparedStatement ps =
                connection.prepareStatement(
                        "UPDATE minions " +
                                "SET name = CONCAT(LOWER(LEFT(name, 1)), SUBSTRING(name, 2)) " +
                                "WHERE id = ?;"
                );
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    private static void printALlMinionsNameAndAge() throws SQLException {
        List<String> minions = new ArrayList<>();
        PreparedStatement ps =
                connection.prepareStatement(
                        "SELECT name, age FROM minions;"
                );

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            minions.add(
                    rs.getString("name") + " " + rs.getInt("age")
            );
        }

        System.out.println(String.join("\n", minions));
    }

    private static void increaseAgeUsingStoredProcedure() throws IOException, SQLException {
        System.out.println("Enter minion ID:");
        int id = Integer.parseInt(reader.readLine());

        CallableStatement callableStatement =
                connection.prepareCall("CALL usp_get_older(?)");
        callableStatement.setInt(1, id);
        callableStatement.executeUpdate();

        PreparedStatement ps =
                connection.prepareStatement(
                        "SELECT name, age FROM minions " +
                                "WHERE id = ?;"
                );
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println(
                    rs.getString("name") + " " + rs.getInt("age")
            );
        }
    }

    private static Connection getConnection() throws IOException, SQLException {
//        System.out.println("Enter username:");
//        String user = reader.readLine().trim();
//
//        System.out.println("Enter password:");
//        String password = reader.readLine().trim();

        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "Java.DB.2021");

        Connection connection =
                DriverManager.getConnection(CONNECTION_STRING, props);

        return connection;
    }
}
