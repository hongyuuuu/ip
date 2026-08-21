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

## Test case 2: Add a task

**Aim:** Verify that a plain-text command creates an incomplete task and confirms the addition.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
buy milk
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
Added: buy milk
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
buy milk
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
Added: buy milk
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [ ] buy milk
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
finish report
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
Added: finish report
____________________________________________________________
____________________________________________________________
Wow you actually got work done...
   [X] finish report
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [X] finish report
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
finish report
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
Added: finish report
____________________________________________________________
____________________________________________________________
Wow you actually got work done...
   [X] finish report
____________________________________________________________
____________________________________________________________
I can't believe you lied to me...
   [ ] finish report
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [ ] finish report
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```
