# Contributing
Glad you want to help us out and make this Discord bot better! If you're planning on contributing, please read this short document to know how you should work on this project.

### Compile using Maven
In order to build the project, you can use the `mvn package` command, which should create a JAR in the `target/` directory.
That JAR needs to be ran using Java 16, and please note that two configuration files are mandatory to start the bot successfully.

### Running the bot
As we mentioned above, two configuration files are mandatory to start the bot successfully.
You need to create a "configs" folder, in which you'll create two files:
- `database_mongo.yml`
- `database.yml`

Here's the default config you should edit:
```yml
type: (MARIADB|MONGODB)
host: (DATABASE HOST IP)
user: (USER)
password: (PASSWORD)
port: (PORT)
databaseName: (DATABASE NAME)
uri: (YOUR URI SCHEMA - Example for MariaDB: `"jdbc:mariadb://{host}:{port}/{databaseName}"`)
```

### Project Branches
This section contains the guildelines to follow when you're commiting to our repository.

The `develop` branch should not be commited to by anyone, except maintainers.
It should always be in a working state, and the only time that branch should be pushed to is when merging a feature branch.

If you want to implement a feature that may take a while to get into a production-ready state, then you should create a dedicated branch following this format: `feat-(short name of the feature)`
If you just stumbled upon a bug, then you need to create a new branch following this format: `fix-(short name of the bug)`

### Contact Us
The easiest way to contact us is by creating an issue here. If you encounter a
bug or have any other problem, open an issue on here for us to take a look at it.
You can also ask for feature requests using those issues.
