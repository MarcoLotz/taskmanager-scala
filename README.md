# Coding Challenge: Task Manager

## Overview

This is a Scala implementation of the Task Manager Coding Challenge.

It holds the same decisions described in ["Original implementation decisions"](https://github.com/MarcoLotz/taskmanager/blob/main/README.md) of the Java project

## Extra Scala-Driven decisions

### ProcessCollector
When implementing the solution in Java, I used a shared interface between all collections.
In java, ArrayList, LinkedList and PriorityQueue implement the Collection interface - which contains mutations behaviours.

This does not happen in Scala.
In Scala, there are immutable collections and mutable.
Unless specified, the collections used are always the immutable ones.
However, there are not 1-1 mappings between mutable and immutable.
For example, PriorityQueue is only available as an mutable collection.
Since Immutable and Mutable collections do not share common operation Traits, I had to create a trait for data manipulations.

In reality, I think this improved the code compared to the Java solution when it comes to Responsability Segregation.
Now, SimpleTaskManager only handles business logics on stored processes.
In parallel to that, implementations of ProcessCollector contains logic on how to handle the specific internal data collection - be it mutable or immutable.

### Exception Handling
Scala is designed to be a Functional Programming Language and thus does not support the concept of checked exceptions.
This makes sense, specially when one thinks about Monadic pipelines.
For operations that may fail (e.g. addProcess in TaskManager) I changed the return type to be Try[Unit].
It's then a problem of the consumer to handle its failures.
I also provided hints of the Runtime Exceptions that could be thrown on Trait definition level.

### Property Testing
By segregating the ProcessCollector from the TaskManager logic, it enabled me to perform different types of test of different components.
For the task manager, I performed Behaviour Driven Test and assured that the expected behaviours hold.

In ProcessCollector implementations, I performed Property Based Testing.
In this case, I used random generators of up to 500 processes to ensure that the properties I would expect from the collectors were respected.
I have defined the properties in an abstract class, reducing the test code footprint.
Only very specific behaviours had to be evaluated on implementation type basis.

### Decorator Pattern as a Sealed Trait
I have used scala Sealed Traits to improve the decorator pattern encapsulation
Instead of having the abstract classes inheritance like originally performed in Java, in Scala it seems to be a good application of sealed traits.
The reasoning is that this Trait won't be freely extended by someone importing the library (fixed number of child classes) and all the implementations only make sense when analysed together.

### Async Logging
Log continues to be async, with lazy evaluation, and with Log4j2.
The only difference is that lazy logging evaluation is enforced as a mixin.