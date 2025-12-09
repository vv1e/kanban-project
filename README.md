# Course Topics Applied
    
* Variables, types, input/output<br>
	* Demonstrated through all  of the project, but here we can see some specific examples:

   
        A group of variables in `KanbanController.java` from lines 46 to 55 that also demonstrate a great variety of types.
  <img width="1596" height="456" alt="image" src="https://github.com/user-attachments/assets/d2dcdfc3-a9d3-42e6-be33-c37b66d7fc8b" />

      
* Control flow: if, switch, loops<br>
	* This concept has been demonstrated in the form of if-statements and loops; here are some examples:

   
        A series of if-statements in `CategorySortController.java` from lines 57 to 76.
  <img width="1012" height="886" alt="image" src="https://github.com/user-attachments/assets/67d616b7-1976-411f-9269-f816b837357e" />

   &nbsp;&nbsp;&nbsp;&nbsp;A for-loop and an if-statement inside the `moveIssue()` method in `Board.java` from lines 127 to 131.
  <img width="1324" height="222" alt="image" src="https://github.com/user-attachments/assets/90975219-d48e-4d81-abcb-262384a8dd48" />


       
* Exceptions
  * For this project, we used exceptions to avoid crashing the app. Here is an example of one:
 
    `LastCategoryDeletionException.java`
    <img width="1454" height="216" alt="image" src="https://github.com/user-attachments/assets/ef860ee8-8c69-4006-86db-1531a09fccac" />


* Methods, parameters, blocks, scope<br>
  	* There are many classes, objects, and methods that were created for this project. Here are some of the most relevant examples of it:


        A method that spans from lines 28 to 136 in `Issue.java`, where one can observe the scope and the parameters.
  <img width="914" height="398" alt="image" src="https://github.com/user-attachments/assets/009cd660-710c-47d5-9451-c598d2c41217" />


* Arrays & ArrayLists<br>
    * Arrays and more frequently ArrayLists were used a lot in this project. Here are some examples:

      A series of arrays of various types in `IssueController.java`, `Category.java`, and `FilteringTask.java` respectively.
<img width="648" height="42" alt="image" src="https://github.com/user-attachments/assets/1042b68d-c8a8-468a-b60a-875af624c41a" />
<img width="1028" height="246" alt="image" src="https://github.com/user-attachments/assets/ecc6549e-962f-4b1b-8e0d-97f5fcc30992" />
<img width="892" height="52" alt="image" src="https://github.com/user-attachments/assets/792a7cbe-6a7d-4805-860a-d0c2fdc133c9" />

  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A series of ArrayLists in `Board.java` and `Category.java`.
  <img width="2014" height="220" alt="image" src="https://github.com/user-attachments/assets/487311a3-4ff8-40c2-a837-264108e6daab" />
  <img width="804" height="168" alt="image" src="https://github.com/user-attachments/assets/bb023d4d-fa32-4f29-9b7a-5226eb61e33b" />


* Objects & classes<br>
   * There are many objects and classes used in this project, many of which have been named in previous examples. Here are some more examples:

        An example of an enum, a type of class, where each of its 3 constants is considered an object. This can be found in `SortField.java` from lines 3 to 7.<br>
     <img width="492" height="224" alt="image" src="https://github.com/user-attachments/assets/76853ac5-9e16-47d7-a975-c9cdb417f38d" />

        The launcher class in `Launcher.java` is another short example of a class.
     <img width="1348" height="712" alt="image" src="https://github.com/user-attachments/assets/a2238cf3-65ac-47ad-9c5e-f3a7799099a4" />



* Inheritance & polymorphism<br>
  * Inheritance and polymorphism are very important concepts for object-based programming. These were used for many parts of our code; here are some examples:

    We use inheritance for exemptions, such as `IssueNotFoundException.java`.
    <img width="1680" height="128" alt="image" src="https://github.com/user-attachments/assets/ab391792-0365-4114-9e36-02d436c384af" />

    `KanbanApplication.java` is a clear example of inheritance, as it extends Application.
    <img width="1824" height="1460" alt="image" src="https://github.com/user-attachments/assets/7b92ebf5-418e-41a8-a8e0-7aebb107dd2a" />

    As can be observed in many of the previous examples, `@Override` is a form of Runtime Polymorphism. Here is another example of it in `IssueController.java` from lines 283 to 295.
    <img width="1552" height="582" alt="image" src="https://github.com/user-attachments/assets/56fdd4c7-cc07-41ca-bc56-ba1199b79116" />




* GUI basics with JavaFX<br>
  * The GUI was implemented with `.fxml` files.

    Here are some examples of both the GUI and `assigne-name-view.fxml`.
    <img width="1666" height="1042" alt="image" src="https://github.com/user-attachments/assets/79529dd2-b58f-4d11-884d-15693eedf3fb" />
    <img width="1316" height="1050" alt="image" src="https://github.com/user-attachments/assets/cc558b58-222c-4447-9cd4-25b0351081a8" />
    <img width="1452" height="1062" alt="image" src="https://github.com/user-attachments/assets/6149a7ee-9360-485b-b01b-e4d9fd981543" />
    <img width="1322" height="1064" alt="image" src="https://github.com/user-attachments/assets/e5d2d745-08cc-4bfa-8950-f0ee56f0c1a7" />

* Files & multithreading<br>
     * Files were implemented in the save/load function, using json files. Multithreading was implemented with the search function.

         The search function can be seen in the top left corner of the GUI. It's implemented thanks to the `FilteringTask.java`.
      <img width="1314" height="1064" alt="image" src="https://github.com/user-attachments/assets/3cef2b04-0aaf-42d0-bd3c-4e73dfe5f875" />
      Threads are also used with javaFX in the GUI.


         Here is an example of a json file that can be read by the program. 

              {
                  "name" : "My Board",
                  "categories" : [ {
                    "name" : "Backlog",
                    "issues" : [ {
                      "name" : "Reindeer",
                      "description" : "Reindeer",
                      "creator" : "Des",
                      "type" : "BUG_REPORT",
                      "id" : 0,
                      "comments" : [ {
                        "author" : "System",
                        "comment" : "Issue created.",
                        "creationDate" : "2025-12-02T22:48:14.07156"
                      }, {
                        "author" : "System",
                        "comment" : "Set assignee to None.",
                        "creationDate" : "2025-12-02T22:52:40.216208"
                      } ],
                      "assignee" : "None",
                      "attachmentPaths" : [ "file:///Users/eva/Downloads/Reindeer.jpg" ]
                    } ]
                  }, {
                    "name" : "In Progress",
                    "issues" : [ ]
                  }, {
                    "name" : "Bug Testing",
                    "issues" : [ ]
                  }, {
                    "name" : "Completed",
                    "issues" : [ {
                      "name" : "Test Save",
                      "description" : "Testing save option",
                      "creator" : "Eva",
                      "type" : "FEATURE_REQUEST",
                      "id" : 1,
                      "comments" : [ {
                        "author" : "System",
                        "comment" : "Issue created.",
                        "creationDate" : "2025-12-02T22:49:26.507077"
                      }, {
                        "author" : "System",
                        "comment" : "Set assignee to Eva.",
                        "creationDate" : "2025-12-02T22:49:34.211928"
                      }, {
                        "author" : "System",
                        "comment" : "Issue moved to \"Bug Testing.\"",
                        "creationDate" : "2025-12-02T22:49:38.001121"
                      }, {
                        "author" : "System",
                        "comment" : "Issue moved to \"Completed.\"",
                        "creationDate" : "2025-12-02T22:50:57.915012"
                      }, {
                        "author" : "System",
                        "comment" : "Set assignee to Eva.",
                        "creationDate" : "2025-12-02T22:52:40.216749"
                      } ],
                      "assignee" : "Eva",
                      "attachmentPaths" : [ ]
                    }, {
                      "name" : "Load Test",
                      "description" : "Test the load/open option.",
                      "creator" : "Eva",
                      "type" : "FEATURE_REQUEST",
                      "id" : 2,
                      "comments" : [ {
                        "author" : "System",
                        "comment" : "Issue created.",
                        "creationDate" : "2025-12-02T22:51:51.577316"
                      }, {
                        "author" : "System",
                        "comment" : "Set assignee to None.",
                        "creationDate" : "2025-12-02T22:52:40.216386"
                      }, {
                        "author" : "System",
                        "comment" : "Issue moved to \"Completed.\"",
                        "creationDate" : "2025-12-02T22:52:51.667906"
                      } ],
                      "assignee" : "None",
                      "attachmentPaths" : [ "file:///Users/eva/Downloads/TestKanban.json" ]
                    } ]
                  }, {
                    "name" : "Won't Do",
                    "issues" : [ ]
                  } ],
                  "categoriesNames" : [ "Backlog", "In Progress", "Bug Testing", "Completed", "Won't Do" ]
                }

* Coding standards & style<br>
  * The style has been demonstrated through all of the project and the examples. All of the variables use lower camel case, and the classes use upper camel case. There are also many comments in the text for clarity reasons.
