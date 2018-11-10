
-- File: companyDML-b-solution 
-- SQL/DML HOMEWORK (on the COMPANY database)
/*
Every query is worth 2 point. There is no partial credit for a
partially working query - think of this hwk as a large program and each query is a small part of the program.
--
IMPORTANT SPECIFICATIONS
--
(A)
-- Download the script file company.sql and use it to create your COMPANY database.
-- Dowlnoad the file companyDBinstance.pdf; it is provided for your convenience when checking the results of your queries.
(B)
Implement the queries below by ***editing this file*** to include
your name and your SQL code in the indicated places.   
--
(C)
IMPORTANT:
-- Don't use views
-- Don't use inline queries in the FROM clause - see our class notes.
--
(D)
After you have written the SQL code in the appropriate places:
** Run this file (from the command line in sqlplus).
** Print the resulting spooled file (companyDML-b.out) and submit the printout in class on the due date.
--
**** Note: you can use Apex to develop the individual queries. However, you ***MUST*** run this file from the command line as just explained above and then submit a printout of the spooled file. Submitting a printout of the webpage resulting from Apex will *NOT* be accepted.
--
*/
-- Please don't remove the SET ECHO command below.
SPOOL companyDML-b.out
SET ECHO ON
-- ------------------------------------------------------------
-- 
-- Name: Adam Stewart
--
-- -------------------------------------------------------------
--
-- NULL AND SUBSTRINGS -------------------------------
--
/*(10B)
Find the ssn and last name of every employee whose ssn contains two consecutive 8's, and has a supervisor. Sort the results by ssn.
*/
SELECT E.LNAME, E.SSN
FROM Employee E
WHERE E.SSN LIKE '%88%' AND E.SUPER_SSN IS NOT NULL
ORDER BY E.SSN;
--
-- JOINING 3 TABLES ------------------------------
-- 
/*(11B)
For every employee who works for more than 20 hours on any project that is controlled by the research department: Find the ssn, project number,  and number of hours. Sort the results by ssn.
*/
SELECT E.SSN, P.PNUMBER, W.HOURS
FROM Employee E, Project P, Works_On W
WHERE E.SSN = W.ESSN AND W.HOURS > 20 AND W.PNO = P.PNUMBER AND P.DNUM = 5
ORDER BY E.SSN;
--
-- JOINING 3 TABLES ---------------------------
--
/*(12B)
Write a query that consists of one block only.
For every employee who works less than 10 hours on any project that is controlled by the department he works for: Find the employee's lname, his department number, project number, the number of the department controlling it, and the number of hours he works on that project. Sort the results by lname.
*/
SELECT E.LNAME, E.DNO, P.PNUMBER, P.DNUM, W.HOURS
FROM Employee E, Project P, Works_On W
WHERE E.SSN = W.ESSN AND 
W.HOURS < 10 AND 
E.DNO = P.DNUM AND 
W.PNO = P.PNUMBER
ORDER BY E.LNAME;
--
-- JOINING 4 TABLES -------------------------
--
/*(13B)
For every employee who works on any project that is located in Houston: Find the employees ssn and lname, and the names of his/her dependent(s) and their relationship(s) to the employee. Notice that there will be one row per qualyfing dependent. Sort the results by employee lname.
*/
SELECT DISTINCT E.SSN, E.LNAME, D.DEPENDENT_NAME, D.RELATIONSHIP
FROM Employee E, Project P, Works_On W, Dependent D
WHERE E.SSN = W.ESSN AND 
W.PNO = P.PNUMBER AND 
P.PLOCATION = 'Houston' AND 
E.SSN = D.ESSN
ORDER BY E.LNAME;
--
-- SELF JOIN -------------------------------------------
-- 
/*(14B)
Write a query that consists of one block only.
For every employee who works for a department that is different from his supervisor's department: Find his ssn, lname, department number; and his supervisor's ssn, lname, and department number. Sort the results by ssn.  
*/
SELECT E1.SSN, E1.LNAME, E1.DNO, E1.SUPER_SSN, E2.LNAME, E2.DNO
FROM Employee E1, Employee E2
WHERE E1.SUPER_SSN = E2.SSN AND 
E1.DNO != E2.DNO
ORDER BY E1.SSN;
--
-- USING MORE THAN ONE RANGE VARIABLE ON ONE TABLE -------------------
--
/*(15B)
Find pairs of employee lname's such that the two employees in the pair work on the same project for the same number of hours. List every pair once only. Sort the result by the lname in the left column in the result. 
*/
SELECT E1.LNAME, E2.LNAME
FROM Employee E1, Employee E2, Works_On W1, Works_On W2
WHERE E1.SSN = W1.ESSN AND 
E2.SSN = W2.ESSN AND 
W1.PNO = W2.PNO AND 
W1.HOURS = W2.HOURS AND 
E1.SSN < E2.SSN
ORDER BY E1.LNAME;
--
/*(16B)
For every employee who has more than one dependent: Find the ssn, lname, and number of dependents. Sort the result by lname.
*/
SELECT E.SSN, E.LNAME, COUNT(*)
FROM Employee E, Dependent D
WHERE E.SSN = D.ESSN
GROUP BY E.SSN, E.LNAME
HAVING COUNT(*) > 1
ORDER BY E.LNAME;
-- 
/*(17B)
For every project that has more than 2 employees working on and the total hours worked on it is less than 40: Find the project number, project name, number of employees working on it, and the total number of hours worked by all employees on that project. Sort the results by project number.
*/
SELECT P.PNUMBER, P.PNAME, COUNT(*), SUM(W.HOURS)
FROM Employee E, Works_On W, Project P
WHERE E.SSN = W.ESSN AND W.PNO = P.PNUMBER
GROUP BY P.PNUMBER, P.PNAME
HAVING COUNT(*) > 2 AND SUM(W.HOURS) < 40
ORDER BY P.PNUMBER;
--
-- CORRELATED SUBQUERY --------------------------------
--
/*(18B)
For every employee whose salary is above the average salary in his department: Find the dno, ssn, lname, and salary. Sort the results by department number.
*/
SELECT D.DNUMBER, E.SSN, E.LNAME, E.SALARY
FROM Employee E, Department D
WHERE E.DNO = D.DNUMBER AND E.SALARY > (SELECT AVG(E.SALARY)
                                        FROM Employee E
                                        WHERE E.DNO = D.DNUMBER)
ORDER BY D.DNUMBER;
--
-- CORRELATED SUBQUERY -------------------------------
--
/*(19B)
For every employee who works for the research department but does not work on any one project for more than 20 hours: Find the ssn and lname. Sort the results by lname
*/
SELECT DISTINCT E.SSN, E.LNAME
FROM Employee E
WHERE E.DNO = 5 AND NOT EXISTS (SELECT W.HOURS
                                FROM Works_On W
                                WHERE E.SSN = W.ESSN AND W.HOURS >= 20)
ORDER BY E.LNAME;
--
-- DIVISION ---------------------------------------------
--
/*(20B) Hint: This is a DIVISION query
For every employee who works on every project that is controlled by department 4: Find the ssn and lname. Sort the results by lname
*/
SELECT E.SSN, E.LNAME
FROM Employee E
WHERE NOT EXISTS (SELECT E.SSN, E.LNAME
                  FROM Works_On W
                  WHERE E.SSN = W.ESSN
                  MINUS
                  SELECT E.SSN, E.LNAME
                  FROM Works_On W
                  WHERE E.SSN = W.ESSN AND (W.PNO = 10 OR W.PNO = 30))
ORDER BY E.LNAME;
--
SET ECHO OFF
SPOOL OFF


