package dao;

public class InitSQL {
    public static String initSQL = "" +
            "CREATE SCHEMA IF NOT EXISTS app" +
            "    AUTHORIZATION postgres;" +
            "CREATE TABLE IF NOT EXISTS app.address " +
            "( " +
            "       id bigserial NOT NULL, " +
            "       version bigint NOT NULL, " +
            "       street text NOT NULL, " +
            "       number_house integer NOT NULL, " +
            "       number_flat integer NOT NULL, " +
            "       CONSTRAINT address_pkey PRIMARY KEY (id)" +
            ");" +
            "CREATE TABLE IF NOT EXISTS app.parent " +
            "(" +
            "    id bigserial NOT NULL, " +
            "    version bigint NOT NULL, " +
            "    name text NOT NULL, " +
            "    id_address bigint NOT NULL, " +
            "    CONSTRAINT parent_pkey PRIMARY KEY (id), " +
            "        CONSTRAINT parent_id_address_fkey FOREIGN KEY (id_address) " +
            "            REFERENCES app.address (id) MATCH SIMPLE " +
            "            ON UPDATE NO ACTION " +
            "            ON DELETE NO ACTION " +
            ");" +
            "CREATE TABLE IF NOT EXISTS app.children " +
            "(" +
            "    id bigserial NOT NULL, " +
            "    version bigint NOT NULL, " +
            "    name text NOT NULL, " +
            "    CONSTRAINT children_pkey PRIMARY KEY (id) " +
            "); " +
            "CREATE TABLE IF NOT EXISTS app.children_parent_cross " +
            "(" +
            "    id_children bigint, " +
            "    id_parent bigint, " +
            "    CONSTRAINT children_parent_cross_id_children_fkey FOREIGN KEY (id_children) " +
            "        REFERENCES app.children (id) MATCH SIMPLE " +
            "        ON UPDATE NO ACTION " +
            "        ON DELETE NO ACTION, " +
            "    CONSTRAINT children_parent_cross_id_parent_fkey FOREIGN KEY (id_parent) " +
            "        REFERENCES app.parent (id) MATCH SIMPLE " +
            "        ON UPDATE NO ACTION " +
            "        ON DELETE NO ACTION " +
            ");" +
            "INSERT INTO app.address (version, street, number_house, number_flat) " +
            "VALUES " +
            "(1,'Street1',1,11)," +
            "(2,'Street2',2,22)," +
            "(3,'Streert',3,33);" +
            "INSERT INTO app.parent (version, name, id_address) " +
            "VALUES " +
            "(1, 'Иван',1)," +
            "(2, 'Петр',1)," +
            "(3, 'Александр',1)," +
            "(4, 'Стас',2)," +
            "(5, 'Илья',2)," +
            "(6, 'Евгений',1)," +
            "(7, 'Виктор',3)," +
            "(8, 'Алексей',3)," +
            "(9, 'Мирон',3)," +
            "(10, 'Клим',3);" +
            "INSERT INTO app.children (version, name) " +
            "VALUES " +
            "(1, 'One'), " +
            "(2, 'Two'), " +
            "(3, 'Tree'), " +
            "(4, 'Four'), " +
            "(5, 'Five'), " +
            "(6, 'Six'), " +
            "(7, 'Seven'), " +
            "(8, 'Eight'), " +
            "(9, 'Nine'), " +
            "(10, 'Ten'), " +
            "(11, 'Eleven'), " +
            "(12, 'Twelve'),  "+
            "(13, 'Thirteen'), " +
            "(14, 'Fourteen'), " +
            "(15, 'Fifteen'); " +
            "INSERT INTO app.children_parent_cross (id_children, id_parent) " +
            "VALUES " +
            "(1, 1)," +
            "(2, 1), " +
            "(3, 1), " +
            "(3, 2), " +
            "(3, 1), " +
            "(4, 4), " +
            "(5, 4), " +
            "(6, 4), " +
            "(7, 4), " +
            "(8, 5) , " +
            "(9, 5), " +
            "(10, 6), " +
            "(11, 1), " +
            "(12, 2), " +
            "(13, 3), " +
            "(14, 4), " +
            "(15, 5);";
}
