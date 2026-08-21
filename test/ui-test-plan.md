# Console UI Test Plan

Run these tests from the repository root with Java 25. Each test case starts a fresh program process. The command is the program-launch command; the fenced input block is sent to standard input exactly as written.

## Test case 1: Exit immediately

**Aim:** Verify that Cookie displays its greeting and exits cleanly when the user enters `bye`.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
bye
```

**Expected output:**
```text
____________________________________________________________
 ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗███████╗
██╔════╝██╔═══██╗██╔═══██╗██║ ██╔╝██║██╔════╝
██║     ██║   ██║██║   ██║█████╔╝ ██║█████╗  
██║     ██║   ██║██║   ██║██╔═██╗ ██║██╔══╝  
╚██████╗╚██████╔╝╚██████╔╝██║  ██╗██║███████╗
 ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚══════╝
Hello! I'm your favourite chatbot Cookie.
What do you need today?
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 2: Add a todo task

**Aim:** Verify that a `todo` command creates an incomplete todo task and confirms the addition.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
todo buy milk
bye
```

**Expected output:**
```text
____________________________________________________________
 ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗███████╗
██╔════╝██╔═══██╗██╔═══██╗██║ ██╔╝██║██╔════╝
██║     ██║   ██║██║   ██║█████╔╝ ██║█████╗  
██║     ██║   ██║██║   ██║██╔═██╗ ██║██╔══╝  
╚██████╗╚██████╔╝╚██████╔╝██║  ██╗██║███████╗
 ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚══════╝
Hello! I'm your favourite chatbot Cookie.
What do you need today?
____________________________________________________________
____________________________________________________________
Ok. I've added this task:
   [T][ ] buy milk
You have 1 tasks now. Better start working.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 3: List tasks

**Aim:** Verify that the `list` command displays all tasks with their numbering and incomplete status.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
todo buy milk
list
bye
```

**Expected output:**
```text
____________________________________________________________
 ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗███████╗
██╔════╝██╔═══██╗██╔═══██╗██║ ██╔╝██║██╔════╝
██║     ██║   ██║██║   ██║█████╔╝ ██║█████╗  
██║     ██║   ██║██║   ██║██╔═██╗ ██║██╔══╝  
╚██████╗╚██████╔╝╚██████╔╝██║  ██╗██║███████╗
 ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚══════╝
Hello! I'm your favourite chatbot Cookie.
What do you need today?
____________________________________________________________
____________________________________________________________
Ok. I've added this task:
   [T][ ] buy milk
You have 1 tasks now. Better start working.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] buy milk
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 4: Mark a task

**Aim:** Verify that `mark 1` marks the first task as complete and displays the completed status.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
todo finish report
mark 1
list
bye
```

**Expected output:**
```text
____________________________________________________________
 ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗███████╗
██╔════╝██╔═══██╗██╔═══██╗██║ ██╔╝██║██╔════╝
██║     ██║   ██║██║   ██║█████╔╝ ██║█████╗  
██║     ██║   ██║██║   ██║██╔═██╗ ██║██╔══╝  
╚██████╗╚██████╔╝╚██████╔╝██║  ██╗██║███████╗
 ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚══════╝
Hello! I'm your favourite chatbot Cookie.
What do you need today?
____________________________________________________________
____________________________________________________________
Ok. I've added this task:
   [T][ ] finish report
You have 1 tasks now. Better start working.
____________________________________________________________
____________________________________________________________
Wow you actually got work done...
   [T][X] finish report
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][X] finish report
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 5: Unmark a task

**Aim:** Verify that `unmark 1` changes a completed task back to incomplete and displays the updated status.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
todo finish report
mark 1
unmark 1
list
bye
```

**Expected output:**
```text
____________________________________________________________
 ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗███████╗
██╔════╝██╔═══██╗██╔═══██╗██║ ██╔╝██║██╔════╝
██║     ██║   ██║██║   ██║█████╔╝ ██║█████╗  
██║     ██║   ██║██║   ██║██╔═██╗ ██║██╔══╝  
╚██████╗╚██████╔╝╚██████╔╝██║  ██╗██║███████╗
 ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚══════╝
Hello! I'm your favourite chatbot Cookie.
What do you need today?
____________________________________________________________
____________________________________________________________
Ok. I've added this task:
   [T][ ] finish report
You have 1 tasks now. Better start working.
____________________________________________________________
____________________________________________________________
Wow you actually got work done...
   [T][X] finish report
____________________________________________________________
____________________________________________________________
I can't believe you lied to me...
   [T][ ] finish report
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] finish report
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 6: Add a deadline task

**Aim:** Verify that a `deadline` command separates the task description from its `/by` date and displays both values.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
deadline return book /by Sunday
bye
```

**Expected output:**
```text
____________________________________________________________
 ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗███████╗
██╔════╝██╔═══██╗██╔═══██╗██║ ██╔╝██║██╔════╝
██║     ██║   ██║██║   ██║█████╔╝ ██║█████╗  
██║     ██║   ██║██║   ██║██╔═██╗ ██║██╔══╝  
╚██████╗╚██████╔╝╚██████╔╝██║  ██╗██║███████╗
 ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚══════╝
Hello! I'm your favourite chatbot Cookie.
What do you need today?
____________________________________________________________
____________________________________________________________
Ok. I've added this task:
   [D][ ] return book (by: Sunday)
You have 1 tasks now. Better start working.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```
