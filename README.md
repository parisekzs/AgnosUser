# AgnosUser
This project implements the user repository of the Agnos BI system. It stores user attributes in property files. The path of the directory that contains the properties files must be specified. 
This directory will contain the following files:
- application-users.properties: It stores the users' attributes. The keys in this property file are the users' names and the values are  JSON strings that describing the other attributes of the users.
- application-roles.properties: It stores the attributes of the roles. The keys in this property file are the names of the roles and the values are JSON strings that describing the other attributes of the roles.
- application-users-roles.properties: It contains the users' roles. The keys in this property file are the users' names and the values are JSON strings that containing a list of roles names belonging to the users.

You can have an overview of our class structure with the diagram below:
![AgnosUser](https://user-images.githubusercontent.com/41894108/114186241-e4c7ec80-9946-11eb-974a-6f14a621a521.png)

- In the entity package, you can find the User, the Role, and the UserRole entities which are persisted in the property files.
- In the repository package, you can find the repository classes, each implements the Spring CrudRepository interface.
- In the model package, you can find the DTO classes.
- In the converter package, you can find the converters between DTO and DAO entities.
