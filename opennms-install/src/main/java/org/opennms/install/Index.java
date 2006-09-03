package org.opennms.install;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Index {

    private String m_name;
    private String m_table;
    private List<String> m_columns;
    private boolean m_unique;

    private static Pattern m_pattern =
            Pattern.compile("(?i)create\\s+(unique\\s+)?"
                            + "index\\s+(\\S+)\\s+"
                            + "on\\s+([^)]+)\\s*\\(([^)]+)\\)");
                              
        /*
        Pattern.compile("(?i)"
                        + "(CREATE TRIGGER (\\S+)\\s+"
                        + "BEFORE (?:INSERT|UPDATE|INSERT OR UPDATE)\\s+"
                        + "ON (\\S+) FOR EACH ROW\\s+"
                        + "EXECUTE PROCEDURE (\\S+)\\(\\));");
                        */


    public Index(String name, String table, List<String> columns,
            boolean unique) {
        m_name = name;
        m_table = table;
        m_columns = columns;
        m_unique = unique;
    }
    
    public static Index findIndexInString(String create) {
        Matcher m = m_pattern.matcher(create.toString());
        if (!m.find()) {
            return null;
        }
        
        boolean unique = (m.group(1) != null);
        String name = m.group(2);
        String table = m.group(3);
        String columnList = m.group(4);
        
        String[] columns = columnList.split("\\s*,\\s*");

        return new Index(name, table, Arrays.asList(columns), unique);
    }
    
    public boolean isOnDatabase(Connection connection) throws SQLException {
        boolean exists;
    
        Statement st = connection.createStatement();
        ResultSet rs = null;
        try {
            rs = st.executeQuery("SELECT relname FROM pg_class "
                                 + "WHERE relname = '" + m_name.toLowerCase()
                                 + "'");
            exists = rs.next();
        } finally {
            if (rs != null) {
                rs.close();
            }
            st.close();
        }
        
        return exists;
    }

    public void removeFromDatabase(Connection connection) throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute("DROP INDEX " + getName());
        } finally{
            st.close();
        }
    }
    
    public void addToDatabase(Connection connection) throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute(getSql());
        } finally{
            st.close();
        }
    }
    
    public String getSql() {
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE ");
        if (m_unique) {
            sql.append("UNIQUE ");
            
        }
        sql.append("INDEX ");
        sql.append(m_name);
        sql.append(" ON ");
        sql.append(m_table);
        sql.append(" ( ");
        sql.append(Installer.join(", ", m_columns));
        sql.append(" )");
        
        return sql.toString();
    }


    public String getName() {
        return m_name;
    }

    public String getTable() {
        return m_table;
    }

}
