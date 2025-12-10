# Kanban Project
A simple JavaFX program emulating the functionality of early Kanban-style management applications.

<img width="1314" height="1064" alt="image" src="https://github.com/user-attachments/assets/3cef2b04-0aaf-42d0-bd3c-4e73dfe5f875" />

Made for a CISC 190 class at San Diego Miramar College.



## Course Topics Applied
    
### Variables, Types, & I/O<br>
* Demonstrated through all  of the project, but here we can see some specific examples:
  
  A group of variables in `KanbanController.java` from lines 46 to 55 that also demonstrate a great variety of types.
  
  <img width="1596" height="456" alt="image" src="https://github.com/user-attachments/assets/d2dcdfc3-a9d3-42e6-be33-c37b66d7fc8b" />

      
### Control flow: `if`, `switch`, & Loops
* This concept has been demonstrated in the form of if-statements and loops; here are some examples:

   
  A series of if statements in `CategorySortController.java` from lines 57 to 76.
  
  <img width="1012" height="886" alt="image" src="https://github.com/user-attachments/assets/67d616b7-1976-411f-9269-f816b837357e" />

  A for loop and an if statement inside the `moveIssue()` method in `Board.java` from lines 127 to 131.
  
  <img width="1324" height="222" alt="image" src="https://github.com/user-attachments/assets/90975219-d48e-4d81-abcb-262384a8dd48" />

       
### Exceptions & Robustness
* For this project, we used exceptions to avoid crashing the app. Here is an example of one:
 
    `LastCategoryDeletionException.java`
  
    <img width="1454" height="216" alt="image" src="https://github.com/user-attachments/assets/ef860ee8-8c69-4006-86db-1531a09fccac" />

* Robustness can be seen in `BoardCreationController`, where the program explicitly verifies that the Board has a valid name, in order to avoid filesystem problems:

  <img width="778" height="652" alt="image" src="https://github.com/user-attachments/assets/c51dbd57-d251-47fc-98d5-29957a5153a5" />


### Methods, parameters, blocks, scope
* There are many classes, objects, and methods that were created for this project. Here are some of the most relevant examples of it:

  A method that spans from lines 28 to 36 in `Issue.java`, where one can observe the scope and the parameters.
  
  <img width="914" height="398" alt="image" src="https://github.com/user-attachments/assets/009cd660-710c-47d5-9451-c598d2c41217" />


### Arrays & ArrayLists
* Arrays, and more frequently ArrayLists are used extensively in this project. Here are some examples:

  - A series of arrays of various types in `IssueController.java`, `Category.java`, and `FilteringTask.java` respectively.
  <br>

  <img width="648" height="42" alt="image" src="https://github.com/user-attachments/assets/1042b68d-c8a8-468a-b60a-875af624c41a" />
  <img width="1028" height="246" alt="image" src="https://github.com/user-attachments/assets/ecc6549e-962f-4b1b-8e0d-97f5fcc30992" />
  <img width="892" height="52" alt="image" src="https://github.com/user-attachments/assets/792a7cbe-6a7d-4805-860a-d0c2fdc133c9" />

  - A series of ArrayLists in `Board.java` and `Category.java`.
  <br>

  <img width="2014" height="220" alt="image" src="https://github.com/user-attachments/assets/487311a3-4ff8-40c2-a837-264108e6daab" />
  <img width="804" height="168" alt="image" src="https://github.com/user-attachments/assets/bb023d4d-fa32-4f29-9b7a-5226eb61e33b" />


### Objects & Classes
* There are many objects and classes used in this project, many of which have been named in previous examples.

* The `ImageAttachmentView` class fulfills the requirement of having objects extend each other, as it is child of `AttachmentView`

  <img width="548" height="199" alt="image" src="https://github.com/user-attachments/assets/7699f550-f76e-45d6-a415-90e04701aacb" />


### Inheritance & Polymorphism
* Inheritance and polymorphism is demonstrated through the `WindowSetup` interface and the classes that extend it:

  	`WindowSetup.java`

	<img width="737" height="65" alt="image" src="https://github.com/user-attachments/assets/c6aaa490-7274-498e-8ef0-db1712e96f75" />

	`IssueController.java` (287-295)

	<img width="806" height="202" alt="image" src="https://github.com/user-attachments/assets/8cab7a03-64db-4a96-aeb5-3ac1152d4fc5" />

	`KanbanController.java` (283-291)

    <img width="675" height="191" alt="image" src="https://github.com/user-attachments/assets/2fdd3399-fcfb-4af3-871b-2d4b23af016c" />


### GUI basics with JavaFX<br>
  * The GUI was implemented with `.fxml` files.

    Here are some examples of both the GUI and `assigne-name-view.fxml`.
    <img width="1666" height="1042" alt="image" src="https://github.com/user-attachments/assets/79529dd2-b58f-4d11-884d-15693eedf3fb" />
    <img width="1316" height="1050" alt="image" src="https://github.com/user-attachments/assets/cc558b58-222c-4447-9cd4-25b0351081a8" />
    <img width="1452" height="1062" alt="image" src="https://github.com/user-attachments/assets/6149a7ee-9360-485b-b01b-e4d9fd981543" />
    <img width="1322" height="1064" alt="image" src="https://github.com/user-attachments/assets/e5d2d745-08cc-4bfa-8950-f0ee56f0c1a7" />

### Files & multithreading
* File I/O and data serialization has been implemented through the use of the Jackson FasterXML Library.

  `KanbanController.java` (329-354)
  
  <img width="718" height="525" alt="image" src="https://github.com/user-attachments/assets/5df245d4-dd08-4a88-b2b6-667a79295f65" />

* Multithreading is implemented through a filtering system, whose functionality is partially outlined in `FilteringTask.java`. It makes use of JavaFX's `Task` class in order to achieve better cooperation between the UI and thread summoned:

  <img width="797" height="687" alt="image" src="https://github.com/user-attachments/assets/3b55589d-ee1a-47d5-a926-4a3b0dca44f9" />



### Coding Standards & Style
* The style has been demonstrated through all of the project and the examples. All of the variables use lower camel case, and the classes use upper camel case. There are also many comments in the text for clarity reasons.
