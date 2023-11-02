<h1 align="center"> TrafficSimulator </h1> <br>

<p align="center">
  Research project to simulate congestion formations and traffic under different circumstances for autonomous vehicles.
</p>


## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [Acknowledgements](#acknowledgements)




## Introduction

[![License](https://en.wikipedia.org/wiki/WTFPL#/media/File:WTFPL_logo.svg)](http://www.wtfpl.net/)

This project's purpose is of purely scientific nature and is in no way associated with Porsche's production systems. Autonomous vehicles automatically determine their required safety distance to other vehicles and adapt their velocity to maintain given distance.  

## Features

* Graphical user interface displaying the cars' movement
* PI and PID controllers available
* Automatic data collection
* Creation of streets and cars during runtime
* Lookahead for vehicles and speed limits


## Requirements
The application can be run locally, the requirements for each setup are listed below.


### Local
* [Java 8 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org/download.cgi)
* [Python](https://www.python.org/downloads/)
  


## Quick Start
Make sure pygame is installed locally to support the startup script's audio feature.

### Configure Pygame
```bash
$ pip install pygame
```

### Run Local
```bash
$ mvn clean package
$ java -jar target/TrafficSimulator-1.0-SNAPSHOT.jar
```


## Acknowledgements
Project created under the supervision of DHBW Stuttgart.
