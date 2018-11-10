-- File PLh20.sql
-- Author: Adam Stewart
-------------------------------------------------------------------
SET SERVEROUTPUT ON
SET VERIFY OFF
------------------------------------
ACCEPT rateDecrement NUMBER PROMPT 'Enter the rate decrement: '
ACCEPT allowedMinRate NUMBER PROMPT 'Enter the allowed min. rate: '

DECLARE
	br boats%ROWTYPE;

	CURSOR bCursor IS
	SELECT B.bid, B.bname, B.color, B.rate, B.length, B.logKeeper
	FROM boats B
	WHERE B.bid NOT IN (SELECT R.bid
						FROM Reservations R
						WHERE R.bid = B.bid);


BEGIN

--Filled br.
	OPEN bCursor;
	LOOP
	FETCH bCursor INTO br;
	EXIT WHEN bCursor%NOTFOUND;
--Prints old rate.
	DBMS_OUTPUT.PUT_LINE ('+++++ Boat: '||br.bid||': old rate = '||br.rate);

	br.rate := br.rate - &rateDecrement;
	DECLARE 
	aboveAllowedMax EXCEPTION;

	BEGIN
	IF br.rate < &allowedMinRate
	THEN RAISE aboveAllowedMax;
	ELSE UPDATE boats
	SET rate = br.rate
	WHERE boats.bid = br.bid;
--Prints successful update.
	DBMS_OUTPUT.PUT_LINE ('----- Boat: '||br.bid||': new rate = '||br.rate);
	END IF;
	EXCEPTION
	WHEN aboveAllowedMax THEN 
--Prints unsuccessful update.
	DBMS_OUTPUT.PUT_LINE ('----- Boat: ' ||br.bid||': Update rejected. The new rate would have been '|| br.rate);
	WHEN OTHERS THEN
	DBMS_OUTPUT.PUT_LINE('+++++ update rejected: ' ||SQLCODE||'...'||SQLERRM);
	END;
	END LOOP;
	COMMIT;

END;
/
