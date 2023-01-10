## Hedgehog PID Documentation (base)

this documentation is for the base Hedgehog PID library *only*, for documentation on abstracted,
higher-level programming using Hedgehog, please reference the High Level Hedgehog documentation
at org.firstinspires.ftc.teamcode/highLevelHedgehog/documentation.md

## INTRO

Hedgehog PID is intended as a more barebones PID implementation than RoadRunner for FTC/FRC. This
library, as well, is intended to be used for FTC robotics, and is not optimized or built for other 
use cases. 

This document was created to revolve around the java files that make Hedgehog work, and to properly
explain each so that anyone using Hedgehog can easily understand how the library works, and to then 
manipulate it to their needs for FTC/FRC. There will be a basic tutorial as well to start users off
creating PID-controlled opmodes as quickly as possible.

You may be wondering why the library is not packaged in a .jar or other external container. This is
because Hedgehog was designed to be directly imported into a codebase and then statically compiled,
as opposed to dynamically loaded. This decision was made since Hedgehog is very small and manageable,
and simply doesn't need to be in a separate library as other, larger libraries need to be.

This library is not intended to be a replacement for RoadRunner. Instead, it aims to aid a 
specific usage within FTC; that being for those who need a quick and easy PID that is easy to learn,
but who dont need all of the fancy controls of RoadRunner, but instead the bare essentials for PID.

Hedgehog was created as a sideproject during the 2022-2023 FTC robotics season, and is not to be 
seen as a complete or fullproof library, and is still a work in progress.

## Tutorial
## driveConstants.java
## marker.java
## PID.java
## segment.java
## trajectory.java