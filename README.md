# Biblioteca

A set of REST API for managing a digital library system

Contents:
* [Setup](#Setup)
* [APIs](#APIs)

## Setup

- **Build project**
  
  `./gradlew build`

- **Run tests**

  `./gradlew test`

- **Run Application**

  `./gradlew bootRun`
  
## APIs
  
 - **Documentation:** https://team1-production.herokuapp.com/swagger-ui/index.html
 
 - **Welcome:** https://team1-production.herokuapp.com/library-management
   "Welcome to the Public Library - Biblioteca !!" message will be displayed.
   
 - **List:** https://team1-production.herokuapp.com/library-management/books
   Lists all books available for checkout.
   
 - **Login:** https://team1-production.herokuapp.com/library-management/login
   A valid librarynumber and password needed to get a success response. Upon receiving 
   a 200 (OK) HTTP response, one can find the bearer token for authorizing secure APIs in the response header
   in the key "Authorization". This token has to provided in the HTTP request header of secure APIs in the key
   "Authorization".
 
 - **Checkout book (secure) :** https://team1-production.herokuapp.com/library-management/checkout
   This API is secure. A valid bearer token is expected in the HTTP Request header in the key "Authorization" 
   for this API. This token can be obtained as explained in the login section above.
   Checks out book which is available for checkout and on successful checkout,
   gives a message "Thank you! Enjoy the book".
   If book is not available, then gives a message "That book is not available".
   
 - **Return Book (secure):** https://team1-production.herokuapp.com/library-management/return
   This API is secure. A valid bearer token is expected in the HTTP Request header in the key "Authorization" 
   for this API. This token can be obtained as explained in the login section above.
   Returns book which has been checked out.
   
 - **List Movies:** https://team1-production.herokuapp.com/library-management/movies
   Lists all available movies in the library for checkout.

 - **Checkout Movie:** https://team1-production.herokuapp.com/library-management/movies/checkout
   Checks out an available movie from the library.
   
 - **User Details:** https://team1-production.herokuapp.com/library-management/user?librarynumber=?
   Fetches the details of an existing library user given the library number. 