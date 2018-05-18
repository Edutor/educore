# Edutor Core Application Server

## Deploying

### MySql

To install and/or run MySql server in Vagrant:
Start a terminal in the `data` folder and write:

```bash
data$ vargrant up
```

The server can be stopped with:

```bash
data$ vagrant halt
```

To access the server:
```bash
data$ vagrant ssh
vagrant@edudata:~$ mysql -uroot -p
Password: _
```

Write the password