-- Insert Employees with explicit IDs
INSERT INTO Employee (id, name) VALUES (1, 'John Doe');
INSERT INTO Employee (id, name) VALUES (2, 'Jane Smith');
INSERT INTO Employee (id, name) VALUES (3, 'John Hoey');
INSERT INTO Employee (id, name) VALUES (4, 'Audrey');
INSERT INTO Employee (id, name) VALUES (5, 'Alford');

-- Insert Addresses with explicit IDs
INSERT INTO Address (id, city) VALUES (1, 'Jakarta');
INSERT INTO Address (id, city) VALUES (2, 'Bogor');
INSERT INTO Address (id, city) VALUES (3, 'Tangerang');
INSERT INTO Address (id, city) VALUES (4, 'Bekasi');
INSERT INTO Address (id, city) VALUES (5, 'Depok');

-- Update Employees with Address IDs
UPDATE Employee SET address_id = 1 WHERE name = 'John Doe';
UPDATE Employee SET address_id = 2 WHERE name = 'Jane Smith';
UPDATE Employee SET address_id = 3 WHERE name = 'John Hoey';
UPDATE Employee SET address_id = 4 WHERE name = 'Audrey';
UPDATE Employee SET address_id = 5 WHERE name = 'Alford';

-- Insert Projects with explicit IDs
INSERT INTO Project (id, project_name, employee_id) VALUES (1, 'Project A', 1);
INSERT INTO Project (id, project_name, employee_id) VALUES (2, 'Project B', 1);
INSERT INTO Project (id, project_name, employee_id) VALUES (3, 'Project C', 2);
INSERT INTO Project (id, project_name, employee_id) VALUES (4, 'Project D', 3);
INSERT INTO Project (id, project_name, employee_id) VALUES (5, 'Project E', 4);
INSERT INTO Project (id, project_name, employee_id) VALUES (6, 'Project F', 5);
--
---- Insert Meetings with explicit IDs
INSERT INTO Meeting (id, meeting_name) VALUES (1, 'Meeting X');
INSERT INTO Meeting (id, meeting_name) VALUES (2, 'Meeting Y');
--
---- Update Employees with Meeting IDs (ManyToMany relationship)
INSERT INTO employee_meeting (employee_id, meeting_id) VALUES (1, 1);
INSERT INTO employee_meeting (employee_id, meeting_id) VALUES (1, 2);
INSERT INTO employee_meeting (employee_id, meeting_id) VALUES (2, 2);
INSERT INTO employee_meeting (employee_id, meeting_id) VALUES (3, 1);
INSERT INTO employee_meeting (employee_id, meeting_id) VALUES (3, 2);
INSERT INTO employee_meeting (employee_id, meeting_id) VALUES (4, 2);
INSERT INTO employee_meeting (employee_id, meeting_id) VALUES (4, 1);
INSERT INTO employee_meeting (employee_id, meeting_id) VALUES (5, 2);
