CREATE USER 'springframework'@'localhost' IDENTIFIED BY 'guru';

GRANT SELECT ON springguru.* to 'springframework'@'localhost';
GRANT INSERT ON springguru.* to 'springframework'@'localhost';
GRANT DELETE ON springguru.* to 'springframework'@'localhost';
GRANT UPDATE ON springguru.* to 'springframework'@'localhost';

CREATE DATABASE qa2;

CREATE USER 'qa2user'@'%' IDENTIFIED BY 'qa2password';

GRANT SELECT ON qa2.* to 'qa2user'@'%';
GRANT INSERT ON qa2.* to 'qa2user'@'%';
GRANT DELETE ON qa2.* to 'qa2user'@'%';
GRANT UPDATE ON qa2.* to 'qa2user'@'%';
