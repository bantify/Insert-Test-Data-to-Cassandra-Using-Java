# Insert test to cassandra cluster using java Application

Pre-requisite: 
Java, cassandra 
Create Maven project is visual studio:
pom.xml file for cassandra driver:
```
    <dependency>
      <groupId>com.datastax.cassandra</groupId>
      <artifactId>cassandra-driver-core</artifactId>
      <version>3.6.0</version>
    </dependency>
```
Properties file to point cassandra DB:
```
cassandra_host=172.23.18.10
cassandra_port=9042
table_dml=INSERT INTO bssapi.call_detail_records_v2 (subscription_id,year,month,event_at,id,b_number,balances) VALUES (?,?,?,?,?,?,?) USING TTL 86400
records=100
```
Table DML:
```
cqlsh> DESC TABLE bssapi.call_detail_records_v2 ;

CREATE TABLE bssapi.call_detail_records_v2 (
    subscription_id text,
    year int,
    month int,
    event_at timestamp,
    id text,
    b_number text,
    balances int,
    PRIMARY KEY ((subscription_id, year, month), event_at, id)
) WITH CLUSTERING ORDER BY (event_at ASC, id ASC)
    AND bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND comment = ''
    AND compaction = {'class': 'org.apache.cassandra.db.compaction.TimeWindowCompactionStrategy', 'compaction_window_size': '6', 'compaction_window_unit': 'HOURS', 'max_threshold': '32', 'min_threshold': '4'}
    AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
    AND crc_check_chance = 1.0
    AND dclocal_read_repair_chance = 0.1
    AND default_time_to_live = 86400
    AND gc_grace_seconds = 5
    AND max_index_interval = 2048
    AND memtable_flush_period_in_ms = 0
    AND min_index_interval = 128
    AND read_repair_chance = 0.0
    AND speculative_retry = '99PERCENTILE';

```
Build Package:
mvn clean package

Run Application:
java -jar demo.jar 
