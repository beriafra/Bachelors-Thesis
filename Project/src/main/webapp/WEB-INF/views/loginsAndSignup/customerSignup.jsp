<!--------------------------------Style--------------------------------->

<style>
    form {
      position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    }
</style>

<!--------------------------------HTML--------------------------------->

<html xmlns:th="http://www.thymeleaf.org">

<form action="/confirm" method="post">
  <label for="Customer ID">Customer ID:</label><br>
  <input type="text" id="Customer ID" name="Customer ID"><br>
  <label for="password">Password:</label><br>
  <input type="password" id="password" name="password"><br>
  <label for="Address">Address:</label><br>
  <input type="text" id="Address" name="Address"><br>
  <label for="Property type">Property type:</label><br>
  <input type="text" id="Property type" name="Property type"><br>
  <label for="Number of bedrooms">Number of bedrooms:</label><br>
  <input type="text" id="Number of bedrooms" name="Number of bedrooms"><br>
  <label for="One valid 8-digit Energy voucher code (EVC)">One valid 8-digit Energy voucher code (EVC):</label><br>
  <input type="text" id="One valid 8-digit Energy voucher code (EVC)" name="One valid 8-digit Energy voucher code (EVC)"><br><br>
  <input type="submit" value="Submit">
</form>

</html>