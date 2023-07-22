package com.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) {

    Properties prop = new Properties();

    try {
      prop.load(new FileInputStream("config.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    Cluster preProdCLuster = Cluster.builder().addContactPoint(prop.getProperty("cassandra_host"))
        .withPort(Integer.parseInt(prop.getProperty("cassandra_port"))).build();
    Session preProdSession = preProdCLuster.connect();
    final Metadata metaData = preProdCLuster.getMetadata();
    final Set<Host> hostList = metaData.getAllHosts();
    for (Host host : hostList) {
      System.out.println(host.getCassandraVersion());
    }
    int records = Integer.parseInt(prop.getProperty("records"));
    for (int i = 0; i < records; i++) {
      String table_dml = prop.getProperty("table_dml");
      PreparedStatement ps = preProdSession.prepare(table_dml);
      List<String> subList = Arrays.asList("8801911223344", "8801922334455", "8801933445566");
      List<Integer> yearList = Arrays.asList(2020, 2021, 2022, 2023);
      List<Integer> monthList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
      List<Integer> balanceList = Arrays.asList(10, 11, 12, 13, 14);
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());

      String sub_id = subList.get(new Random().nextInt(subList.size()));
      Integer year = yearList.get(new Random().nextInt(yearList.size()));
      Integer month = monthList.get(new Random().nextInt(monthList.size()));
      Integer balance = balanceList.get(new Random().nextInt(balanceList.size()));
      BoundStatement bs = ps.bind(sub_id, year, month, timestamp, sub_id, sub_id, balance);
      preProdSession.execute(bs);

      System.out.println("Sub id:" + sub_id + " inserted....");
    }
    preProdSession.close();
    preProdCLuster.close();
  }
}
