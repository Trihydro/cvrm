# Connected Vehicle Resource Manager
This application was developed with the support of the USDOT ITS Joint Program Office.

## Description

This application provides a website to manage resources associated with a Connected Vehicle project. The website provides a series of forms to enter and view resources such as participants, training courses, vehicles and devices that are part of the project. Graphs showing resource usage and device deployment are provided in the website. Role based access restrictions limit the visibility of data to specific user roles. 

The application consists of a website, a REST service and a backend database. The website and REST service are secured using ssl security. User login security is provided using the Auth0 identity platform.

## Release Notes

### Iteration 5
- Improved email styling on user invites
- Now housing service enpoints and website access in SSL environments 
- Data redaction implemented for users without permissions to view PII across the website

### Iteration 4
- Integrated User Roles and their access levels across the site per page. 
- Added a user settigns page for resetting a ones's password
- Implemented super user pages for user with elevated permissions to edit, block, and invite other users.
- Developed user creation functionality that emails invited users and allows them to reset their password among first logging in. 
- Integrated securty across the site using Auth0 to handle endpoint authorization, page permissions, and login/logout functionality.

### Iteration 3
- Implemented the functionality to add/edit/delete training courses.
- Developed an interactive dashboard to provide snapshots of information regarding participant training statuses, vehicles in use, and deployed equipment. 
- Added the ability to add and remove training for participants and the current status of each course per participant.

### Iteration 2
- Implemented the functionality to add/edit/delete participants and vehicles.
- Integrated feedback to remove vehicle types, expand vehicle classes, add notes fields to vehicles and equipment, and added VIN numbers to vehicles. 

### Iteration 1
- Implemented the functionality to add/edit/delete equipment and equipment components.

## Getting Started

### Prerequisites
MySQL

Auth0 Authentication Account

### Instructions

The follwing instructions describe the processes to download, build, and run the website.

#### Downloading the source code

**Step 1**: Clone the solution from GitHub using:

```bash
git clone https://github.com/Trihydro/cvrm.git
```

**Step 2**: Start MySQL in a terminal

**Step 3**: Create the database in MySQL

```bash
mysql> create database cvpt;
```

**Step 4**: Create database tables with script

```bash
mysql> use cvpt;
mysql> source <project install location>/data/cvptdbcreate;
```

**Step 5**: Replace all instances of &lt;username&gt; and &lt;password&gt; the file cvrm/data/testData.loadData.cmd with your MySQL username and password. 
Replace the instances of &lt;db-username&gt; and &lt;db-password&gt; in the file cvrm/service/src/main/resources/application.properties with your MySQL username and password.


**Step 6**: Load test data in DOS/Bash terminal
```bash
cd cvrm/data/data/testData
loadData.cmd
```

**Step 7**: Create the user for the database in MySQL shell

```bash
mysql> CREATE USER 'cvpt-user'@'localhost' IDENTIFIED BY 'cvptuser';
mysql> GRANT ALL PRIVILEGES ON cvpt . * TO 'cvpt-user'@'localhost';
```

**Step 8**: Set up an Auth0 account with a Single Page Application service for the website logins and a Machine to Machine service for the web site to connect with the REST service. Record the Client Id, ClientSecret for the Single Page Application service and ApiClientId, ApiClientSecret for the Machine to Machine service. 

**Step 9**: Replace instances of the &lt;auth0-Domain&gt;, &lt;auth0-Issuer&gt;,&lt;auth0-ClientId&gt;, &lt;auth0-ClientSecret&gt;, &lt;auth0-ApiClientId&gt;, and &lt;auth0-ApiClientSecret&gt; found in the file cvrm/service/src/main/resources/auth0.properties with corresponding values returned from your Auth0 configuration.

**Step 10**: For ssl security set up a Java Key Store with appropriate certificates for the selected domain name. Create the Java Key Store as the file cvrm/service/src/main/resources/cvrmServerKeyStore.jks. 

**Step 11**: Replace the instances of &lt;key-store-password&gt; and &lt;ssl-alias&gt; found in the file cvrm/service/src/main/resources/application.properties with values set up for your Java Key Store and domain. 

**Step 12**: Replace the instance of &lt;mail-host-ip-address&gt; found in the file cvrm/service/src/main/resources/application.properties with the ip address of the email server that will be used to email user login invitations.

**Step 13**: Replace the image files found in /cvrm/website/src/main/website/src/ with pictures and logos appropriate for your CV project.

**Step 14**: Build the entire project in DOS/Bash terminal

```bash
cd cvrm
mvn clean install
```

**Step 15**: Start the service

```bash
cd cvrm/service
mvn spring-boot:run
```

**Step 16**: Start the website in a separate terminal

```bash
cd cvrm/website/src/main/website
npm start
```




