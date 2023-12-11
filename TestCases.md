# CS180-BLK-Project-5
## Test Cases

### Test 1: Customer create account
Steps:

1. User runs MarketPlaceServer
2. User runs MarketPlace Client
3. User selects CUSTOMER
4. User selects Create Account
5. User selects the Username textbox
6. User enters "user1@gmail.com" via the keyboard
7. User selects the password textbox
8. User enters "1234" via the keyboard
9. User selects `Login/Create Account` button

Expected Result: Popup confirms that account has been created and the Customer MarketPlace is launched

Test Status: Passed

### Test 2: Customer Login
Steps:

1. User runs MarketPlaceServer
2. User runs MarketPlace Client
3. User selects CUSTOMER
4. User selects Login
5. User selects the Username textbox
6. User enters "user1@gmail.com" via the keyboard
7. User selects the password textbox
8. User enters "1234" via the keyboard
9. User selects `Login/Create Account` button

Expected Result: Popup confirms that User has logged in and the Customer MarketPlace is launched

Test Status: Passed

### Test 3: Invalid Email Format
Steps:

1. User runs MarketPlaceServer
2. User runs MarketPlace Client
3. User selects CUSTOMER
4. User selects Create Account
5. User selects the Username textbox
6. User enters "user1" via the keyboard
7. User selects the password textbox
8. User enters "1234" via the keyboard
9. User selects `Login/Create Account` button

Expected Result: Popup says "Must Be A Valid Email"

Test Status: Passed

### Test 4: Wrong Password
Steps:

1. User runs MarketPlaceServer
2. User runs MarketPlace Client
3. User selects CUSTOMER
4. User selects Create Account
5. User selects the Username textbox
6. User enters "user1@gmail.com" via the keyboard
7. User selects the password textbox
8. User enters "123" via the keyboard
9. User selects `Login/Create Account` button

Expected Result: Popup says "Wrong Password For This Account"

Test Status: Passed

### Test 5: Exit
Steps:
1. User goes through Customer Login `user1@gmail.com` : `1234`
2. User presses the `Exit` button

Expected Result: Exits the GUI interface

Test Status: Passed

### Test 6: Change Username
Steps:
1. User goes through Custome Login `user1@gmail.com` : `1234`
2. User presses the `Edit Account` button
3. User selects Change Username
3. User selects the old textbox
4. User enters "user1@gmail.com" via the keyboard
5. User selects the new textbox
6. User enters "customer@gmail.com"
7. User selects the `Change Username/Password` button

Expected Result: Popup confirms that Username is changed and returns to the Customer MarketPlace

Test Status: Passed

### Test 7: Change Password
Steps:
1. User goes through Customer Login `customer@gmail.com` : `1234`
2. User presses the `Edit Account` button
3. User selects Change Username
3. User selects the old textbox
4. User enters "1234" via the keyboard
5. User selects the new textbox
6. User enters "123"
7. User selects the `Change Username/Password` button

Expected Result: Popup confirms that Password is changed and returns to the Customer MarketPlace

Test Status: Passed

### Test 8: Seller create account
Steps:

1. User runs MarketPlaceServer
2. User runs MarketPlace Client
3. User selects SELLER
4. User selects Create Account
5. User selects the Username textbox
6. User enters "seller@gmail.com" via the keyboard
7. User selects the password textbox
8. User enters "1234" via the keyboard
9. User selects `Login/Create Account` button

Expected Result: Popup confirms that account has been created and the Seller MarketPlace is launched

Test Status: Passed

### Test 9: Seller Login
Steps:

1. User runs MarketPlaceServer
2. User runs MarketPlace Client
3. User selects SELLER
4. User selects Login
5. User selects the Username textbox
6. User enters "seller@gmail.com" via the keyboard
7. User selects the password textbox
8. User enters "1234" via the keyboard
9. User selects `Login/Create Account` button

Expected Result: Popup confirms that User has logged in and the Seller MarketPlace is launched

Test Status: Passed

### Test 10: Back Button
Steps:
1. User goes through Seller Login `seller@gmail.com` : `1234`
2. User presses the `Add Products` button
3. User selects the `Back Button` button

Expected Result: Returns to the Seller MarketPlace
Test Status: Passed

### Test 11: Add Products
Steps:
1. User goes through Seller Login `seller@gmail.com` : `1234`
2. User presses the `Add Products` button
3. User selects Enter Product Name textbox
4. User enters "iPhone" via the keyboard
5. User selects Enter Product Price textbox
6. User enters "1000" via the keyboard
7. User selects Enter Product Quantity textbox
8. User enters "500" via the keyboard
9. User selects Product Store textbox
10. User enters "Apple" via the keyboard
11. User selects Enter Product Description textbox
12. User enters "SmartPhone" via the keyboard
13. User selects the `Add Product` button

Expected Result: Popup confirms that prodcut is added and returns to the Seller MarketPlace. Also displays the product on the page

Test Status: Passed

    Link Tests 12 to 14 to properly test concurrency

### Test 12: Concurrency
Steps:
1. User runs MarketPlaceServer
2. User runs MarketPlaceClient
3. User goes through Seller login `seller@gmail.com` : `1234`
4. User runs MarketPlaceClient
5. User goes through Customer Login `customer@gmail.com` : `123` 

Expected Result: There should be two GUI interfaces, one of Seller MarketPlace and one of Customer MarketPlace

Test Status: Passed

### Test 13: Modify Product
Steps:
1. Continue from Test 12. User goes to Seller MarketPlace
2. User presses the `Modify Products` button next to iPhone
3. User selects Enter Product Name textbox
4. User enters "iPhone 15" via the keyboard
5. User selects Enter Product Price textbox
6. User enters "800" via the keyboard
7. User selects Enter Product Quantity textbox
8. User enters "500" via the keyboard
9. User selects Product Store textbox
10. User enters "Apple" via the keyboard
11. User selects Enter Product Description textbox
12. User enters "New SmartPhone" via the keyboard
13. User selects the `Modify Product` button: `123`

Expected Result: Popup confirms that product is modified and returns to the Seller MarketPlace. Also displays the product on the page with all the new information

Test Status: Passed

### Test 14: Refresh
Steps:
1. Continue from Test 13. User goes to Customer MarketPlace
2. User selects the `Refresh` button

Expected Result: Product "iPhone" and stats should update to "iPhone 15" and the new stats

Test Status: Passed