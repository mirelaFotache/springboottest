BOOKSTORE

OVERVIEW

Bookstore is a web application having as main goal the management of Pentalog internal library. 
Application offers a list of all the available books in the library, with the possibility of reserving them for a period of time.
Authorized users can reserve books, add ratings and comments about the reserved books and view ratings or comments other users have already given for the books they have read.
If they don't return the books in time, they are notified periodically about about the delay.

The application relay on a postgres database containing the following tables:
- users - stores all the users which have access to the library, containing information as:
    First name, last name, username and password, address, email, phone number and if user is active or not
- role - stores the name of the roles allowed in the application: admin, user etc
    Only admin users have access to edit users, roles, categories and books
    Common users can only view the list of books, make bookings, and add ratings about the books they reserved
    Roles allowed in the application can be edited in WebSecurityConfig class which maps the authorized roles on the available endpoints
        like    .antMatchers("/users/**", "/roles/**").hasRole("ADMIN")
    Once a new role is inserted in the database, it must be added in WebSecurityConfig class too!
- category - stores the category each book may belong to
- book - stores all the books contained by the library and offer information as the author of the book, title, date when the book was published,
    image, location (where the book can be found), stock indicating how many books of that type are in the library and stock of the books
    that were not yet reserved at a given time
- booking - offers a history of all the books that were reserved and by whom. So it contains the id of the user and the id of the reserved book
    the date when the reservation was made, an estimated time when the book should be returned and the real date when the book was returned.
- rating - offers the possibility to the users to give a feedback about the book they have read. Table stores the id of the user and the 
    id of the reserved book, a comment added by the user about how useful that book was, and a rating (a note that user give to the book) 

Database parameters defined in application-dev.yml are:
    spring.datasource.url: jdbc:postgresql://localhost:5432/bookstore
    spring.datasource.username: postgres
    spring.datasource.password: admin

Junit tests use an in memory H2 database. Specific parameters are stored in resources/application.properties file:
    spring.datasource.url=jdbc:h2:file:~/dasbootTestMarina
    spring.datasource.username=sa
    spring.datasource.password= 
For tests only, embedded Tomcat use a different port defined in the same file: server.port=8888
 
In order for the users to be able to use the application they must login. The session is active for a defined period of time and then it expires.
The timeout is defined in application-dev.yml file being set by this parameter -     server: servlet: session: timeout: 2

Application also contains internationalization, messages being displayed in one of the languages english or french.
Parameter that define the default language is sored in application-dev.yml as - language.selectedLanguage: fr_FR
If one more language must be available then an entry must be also added in InternationalizationConfig class, in localeResolver method:
        if (("fr_FR").equals(selectedLanguage))
            lr.setDefaultLocale(Locale.FRANCE);
        else {
            lr.setDefaultLocale(Locale.US);
        }
Also, a new properties file containing the messages translated into the desired language must be created. The name of the file should respect
the format: language_xx_XX.properties where xx_XX indicates the new language.

Notifications that users receive when they not return the books in time are stored in SchedulerConfig class. 
This is where someone could define the duration between notifications.


VALIDATIONS

One user can not book more than a defined number of books (each book has real_end_date null while it is booked)
One user can not have two active bookings on the same book
Only admin users can edit users,roles, categories and books. 
Common users can view books, make reservations and add comments/ratings for the books they have reserved
You cannot insert two users with the same nickname
Inactive users can not login in the application


AVAILABLE ENDPOINTS

BOOKS
1. Display all books from the library
http://localhost:8080/bookstore/books
2. Find books by title, author and availability (0 for available true and 1 for available false)
http://localhost:8080/bookstore/books/bookingPreferences?title=&author=Goncalves&availability=0
3. Display all categories where a book is registered
http://localhost:8080/bookstore/books/{bookId}/categories
4. Find all bookings for given book (show history of reservations made for the book)
http://localhost:8080/bookstore/books/{bookId}/bookings
5. Find book by book id
http://localhost:8080/bookstore/books/{bookId}
6. Find book by title
http://localhost:8080/bookstore/books/title?searchBy=Head First Java
7. Find books by author
http://localhost:8080/bookstore/books/author?searchBy=Goncalves
8. Find all books. For each book display the most recent booking if any was found and the user who reserved the book
http://localhost:8080/bookstore/books/booksAvailability
9. Endpoints for insert, update, delete

BOOKINGS
1. Display all books that one user has not returned yet
http://localhost:8080/bookstore/bookings/activeBookingsPerUser?userId=1
2. Display all bookings
http://localhost:8080/bookstore/bookings
3. Endpoints for insert, update, delete

RATINGS
1. Get all ratings (comments) that one user has given to the books he has reserved
http://localhost:8080/bookstore/ratings/{userId}/ratingsPerUser
2. Get all ratings (comments) associated with given book
http://localhost:8080/bookstore/ratings/{bookId}/ratingsPerBook
3. Display all ratings(comments) ever made
http://localhost:8080/bookstore/ratings
4. Get all ratings per user and book id
http://localhost:8080/bookstore/ratings/preferences?user_id=8&book_id=5
5. Endpoints for insert, update, delete

CATEGORIES 
1. Find category by name. Show the name and the id of the category
http://localhost:8080/bookstore/categories/name?searchBy=java
2. Display all categories. Show the name and the id of each category
http://localhost:8080/bookstore/categories
3. Endpoints for insert, update, delete

USERS - Only admin users can access following endpoints
1. Find user by user name
http://localhost:8080/bookstore/users/name?searchBy=Mirela
2. Get user by id
http://localhost:8080/bookstore/users/{userId}
3. Get all users
http://localhost:8080/bookstore/users
4. Endpoints for insert, update, delete

ROLES - Only admin users can access following endpoints
1. Find role by name
http://localhost:8080/bookstore/roles/name?searchBy=admin
2. Find roles one user have based on user id
http://localhost:8080/bookstore/roles/{userId}/roles
3. Find all available roles
http://localhost:8080/bookstore/roles

