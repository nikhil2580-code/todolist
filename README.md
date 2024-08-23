1. Project Overview
The Task Management App allows users to manage tasks and categories. Features include task creation, editing, deadline setting, completion marking, and category organization. This document outlines the code structure, libraries used, and development approach.

2. Code Structure
   MainActivity.kt: Initializes RecyclerViews for categories and tasks, and handles task operations (add, delete, edit, update completion status).

Adapters:

CategoryAdapter.kt: Displays categories in RecyclerView and manages interactions.
TaskAdapter.kt: Displays tasks, manages operations like delete, edit, and mark as complete.
Models:

Task.kt: Represents a task with properties (id, name, category, deadline, isComplete).
Category.kt: Represents a category with properties (id, name).
Data:

TaskDao.kt: DAO for SQLite database interactions (insert, update, delete, retrieve tasks and categories).
TaskDatabase.kt: Room database class for managing SQLite data.
UI Layouts:

activity_main.xml: UI for MainActivity with RecyclerViews and FloatingActionButton.
task_item.xml: Layout for individual task items.
category_item.xml: Layout for individual category items.

3. Libraries Used
   Room (Android Jetpack):

Purpose: ORM library for managing SQLite database.
Classes: TaskDao, TaskDatabase.
Functionality: Simplifies database operations and queries.
Lifecycle Components (Android Jetpack):

Purpose: Observes LiveData and updates the UI reactively.
Classes: Observer, LifecycleScope.
Functionality: Ensures UI components update with data changes.
RecyclerView:

Purpose: Displays lists of tasks and categories efficiently.
Classes: RecyclerView, RecyclerView.Adapter, LinearLayoutManager.
Functionality: Handles dynamic lists and scrolling.
Kotlin Coroutines:

Purpose: Manages asynchronous tasks.
Classes: lifecycleScope, launch.
Functionality: Executes long-running tasks in the background, keeping the UI responsive.