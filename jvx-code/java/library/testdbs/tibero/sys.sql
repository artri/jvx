CREATE OR REPLACE PACKAGE diutil IS

  FUNCTION bool_to_int( b BOOLEAN) RETURN NUMBER;

  function int_to_bool( n NUMBER) return boolean;

end diutil;

CREATE OR REPLACE PACKAGE BODY diutil IS

  FUNCTION bool_to_int(b BOOLEAN) RETURN NUMBER IS
  BEGIN
    IF b THEN
      RETURN 1;
    ELSIF NOT b THEN
      RETURN 0;
    ELSE
      RETURN NULL;
    END IF;
  END bool_to_int;

  FUNCTION int_to_bool(n NUMBER) RETURN BOOLEAN IS
  BEGIN
    IF n IS NULL THEN
      RETURN NULL;
    ELSIF n = 1 THEN
      RETURN true;
    ELSIF n = 0 THEN
      RETURN false;
    ELSE
      RAISE value_error;
    END IF;
  END int_to_bool;

end diutil;

grant all on diutil to public;