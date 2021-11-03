# dummy-service

## Description

**dummy-service** is a very simple C program designed to test some features and
configurations of [systemd](https://www.freedesktop.org/wiki/Software/systemd/)
specially those related to the restart of the service when it terminates
abruptly without any interaction with **systemd** itself.

The program just starts and wait for a signal telling it how it should be
terminated. Nothing more, nothing less.

## Building it

To build this program, you will need [CMake](https://cmake.org), a the systme's
C compiler and the **pthreads** library. This program has been designed to work
on a Linux box but may be used in a **FreeBSD** and even **MacOS** but I did not
test it on those systems.

To build this program, follow these steps:

1. Create a subdirectory called `build`;
2. Open a terminal inside this directory;
3. Run `cmake ..` to initialize the building process;
4. Run `cmake --build .` to build the program;

The executable will be generated on this directory under the name `dummy-service`.

## How to use

### Installation

This program, as stated in the description, has been created to test **systemd**,
thus, it is intended to be executed under it as a service unit like this:

```
[Unit]
Description=dummy service
# Limit the restarts 5 during a 5 minute interval. If the process dies more than 5 times within
# 5 minutes, the process will not be restarted.
#See https://www.freedesktop.org/software/systemd/man/systemd.unit.html#StartLimitIntervalSec=interval
StartLimitIntervalSec=300s
StartLimitBurst=5

[Service]
ExecStart=<path to the executable>/dummy-service
# Waits up to 15s before restart
# See https://www.freedesktop.org/software/systemd/man/systemd.service.html#RestartSec=
RestartSec=15s
# Restart when the process ends with a non-zero exit code or a term.
# See https://www.freedesktop.org/software/systemd/man/systemd.service.html#Restart=

[Install]
WantedBy=multi-user.target
```

To install it, you can create a file named `/etc/systemd/system/dummy-service.service`
with the contents of the service definition.

Once this is done, run as root `systemctl daemon-reload` to reload the **systemd**
and force it to load the new service.

## Playing with it

Once installed, the service is ready to be used. As root, start the service with
the command `systemctl start dummy-service`. This will bring this dummy service 
up and ready to *crash*.

This service does nothing but wait for signals to decide what it should do. It
will react to all signals as expected by the default signal handler except for
the following signals:

- `SIGTERM`: Terminates with the return code 0;
- `SIGUSR1`: crash with a division by zero;
- `SIGUSR2`: crash with an access violation;

To send this signals to the service, run `kill sinal pid_of_the_service` where
signal may be `-TERM`, `-USR1`, `-USR2` or any other signal you wish.

Once this is done, you can check the status of the service using the command
`systemctl status dummy-service`.

With the above configuration, the service should restart within the next 15
seconds unless you kill it more than 5 times within 5 minutes or it exits with
return code 0.

Feel free to play with those parameters.

## License

The contents of this directory is released to **public domain**. Feel free to
do whatever you want with it. See [LICENSE](LICENSE) for further information.
