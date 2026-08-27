# Console UI Test Plan

Run these tests from the repository root with Java 25. Compile the current source before running the plan:

```text
javac -encoding UTF-8 -d out src/main/java/*.java
```

Each test case starts a fresh program process. The command is the program-launch command; the fenced input block is sent to standard input exactly as written.

## Coverage guide

| Area | Successful behavior | Validation and state preservation |
| --- | --- | --- |
| Application flow | 1 | 9, 12--14 |
| Todo and list | 2--3 | 8, 13, 26 |
| Completion state | 4--5, 25 | 11, 15--17, 22 |
| Deadline and event | 6--7 | 10, 18, 23--24 |
| Deletion | 19 | 20--22 |

The expected output in every case is compared exactly, including spaces and separators.

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
You have 1 task(s) now. Better start working.
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
You have 1 task(s) now. Better start working.
____________________________________________________________
____________________________________________________________
Here are the task(s) in your list:
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
You have 1 task(s) now. Better start working.
____________________________________________________________
____________________________________________________________
Wow you actually got work done...
   [T][X] finish report
____________________________________________________________
____________________________________________________________
Here are the task(s) in your list:
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
You have 1 task(s) now. Better start working.
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
Here are the task(s) in your list:
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
You have 1 task(s) now. Better start working.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 7: Add an event task

**Aim:** Verify that an `event` command separates the task description from its `/from` and `/to` times and displays all three values.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
event project meeting /from Mon 2pm /to 4pm
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
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
You have 1 task(s) now. Better start working.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 8: Reject a todo without a description

**Aim:** Verify that an empty `todo` description displays an error and allows the user to continue.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
todo
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
Bruh... A todo task needs a description.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 9: Reject an unknown command

**Aim:** Verify that an unrecognized command displays an error and does not terminate Cookie.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
blah
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
Bruh... What is that command!?
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 10: Reject an incomplete deadline

**Aim:** Verify that a `deadline` without a `/by` date displays an error and does not add a task.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
deadline return book
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
Bruh... A deadline needs a description and a date after /by.
____________________________________________________________
____________________________________________________________
Here are the task(s) in your list:
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 11: Reject an invalid task number

**Aim:** Verify that marking a task number that is not in the list displays an error and does not terminate Cookie.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
mark 1
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
Bruh... There is no task numbered 1.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 12: Reject an empty command

**Aim:** Verify that a blank input line displays an error and allows the user to continue.

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
Bruh... I couldn't understand an empty command.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 13: Reject extra arguments for list

**Aim:** Verify that the `list` command rejects unexpected arguments.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
list now
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
Bruh... The list command does not take any arguments.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 14: Reject extra arguments for bye

**Aim:** Verify that the `bye` command rejects unexpected arguments and remains available afterward.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
bye now
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
Bruh... The bye command does not take any arguments.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 15: Reject a missing mark number

**Aim:** Verify that `mark` requires a task number.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
mark
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
Bruh... Usage: mark <task number>.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 16: Reject a missing unmark number

**Aim:** Verify that `unmark` requires a task number.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
unmark
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
Bruh... Usage: unmark <task number>.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 17: Reject a non-numeric task number

**Aim:** Verify that a task number must be a positive whole number.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
mark first
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
Bruh... The task number must be a positive whole number.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 18: Reject an incomplete event

**Aim:** Verify that an `event` requires a description, a `/from` start time, and a `/to` end time.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
event project meeting /from Mon 2pm
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
Bruh... An event needs a description, a start time after /from, and an end time after /to.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 19: Delete a task

**Aim:** Verify that `delete` removes the selected task, reports the removed task and remaining count, and renumbers the remaining tasks.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
todo buy groceries
deadline submit report /by Friday
event team meeting /from Mon 2pm /to 3pm
delete 2
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
   [T][ ] buy groceries
You have 1 task(s) now. Better start working.
____________________________________________________________
____________________________________________________________
Ok. I've added this task:
   [D][ ] submit report (by: Friday)
You have 2 task(s) now. Better start working.
____________________________________________________________
____________________________________________________________
Ok. I've added this task:
   [E][ ] team meeting (from: Mon 2pm to: 3pm)
You have 3 task(s) now. Better start working.
____________________________________________________________
____________________________________________________________
You're welcome. I've gotten rid of this task for you:
   [D][ ] submit report (by: Friday)
Now you have 2 task(s) in the list.
____________________________________________________________
____________________________________________________________
Here are the task(s) in your list:
1. [T][ ] buy groceries
2. [E][ ] team meeting (from: Mon 2pm to: 3pm)
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 20: Reject an invalid delete number

**Aim:** Verify that `delete` rejects a task number that is not in the list and keeps Cookie running.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
delete 1
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
Bruh... There is no task numbered 1.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 21: Reject a missing delete number

**Aim:** Verify that `delete` requires a task number.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
delete
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
Bruh... Usage: delete <task number>.
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 22: Reject boundary task numbers without changing the list

**Aim:** Verify that zero, negative, out-of-range, and extra task-number arguments are rejected while the existing task remains unchanged.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
todo keep me
mark 0
unmark -1
delete 2
mark 1 extra
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
   [T][ ] keep me
You have 1 task(s) now. Better start working.
____________________________________________________________
____________________________________________________________
Bruh... There is no task numbered 0.
____________________________________________________________
____________________________________________________________
Bruh... There is no task numbered -1.
____________________________________________________________
____________________________________________________________
Bruh... There is no task numbered 2.
____________________________________________________________
____________________________________________________________
Bruh... Usage: mark <task number>.
____________________________________________________________
____________________________________________________________
Here are the task(s) in your list:
1. [T][ ] keep me
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 23: Reject malformed deadline fields

**Aim:** Verify that a deadline requires both a description and a date after the `/by` marker, and that invalid input does not add a task.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
deadline /by Friday
deadline submit report /by
deadline submit report by Friday
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
Bruh... A deadline needs a description and a date after /by.
____________________________________________________________
____________________________________________________________
Bruh... A deadline needs a description and a date after /by.
____________________________________________________________
____________________________________________________________
Bruh... A deadline needs a description and a date after /by.
____________________________________________________________
____________________________________________________________
Here are the task(s) in your list:
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 24: Reject malformed event fields

**Aim:** Verify that an event requires a description, a non-empty `/from` time, a non-empty `/to` time, and correctly ordered markers without adding a task on error.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
event /from Mon 2pm /to 3pm
event team meeting /from /to 3pm
event team meeting /from Mon 2pm /to
event team meeting /to 3pm /from Mon 2pm
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
Bruh... An event needs a description, a start time after /from, and an end time after /to.
____________________________________________________________
____________________________________________________________
Bruh... An event needs a description, a start time after /from, and an end time after /to.
____________________________________________________________
____________________________________________________________
Bruh... An event needs a description, a start time after /from, and an end time after /to.
____________________________________________________________
____________________________________________________________
Bruh... An event needs a description, a start time after /from, and an end time after /to.
____________________________________________________________
____________________________________________________________
Here are the task(s) in your list:
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 25: Change the completion state of deadline and event tasks

**Aim:** Verify that `mark` and `unmark` preserve the type-specific details of deadline and event tasks.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
deadline submit report /by Friday
event team meeting /from Mon 2pm /to 3pm
mark 1
mark 2
unmark 2
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
   [D][ ] submit report (by: Friday)
You have 1 task(s) now. Better start working.
____________________________________________________________
____________________________________________________________
Ok. I've added this task:
   [E][ ] team meeting (from: Mon 2pm to: 3pm)
You have 2 task(s) now. Better start working.
____________________________________________________________
____________________________________________________________
Wow you actually got work done...
   [D][X] submit report (by: Friday)
____________________________________________________________
____________________________________________________________
Wow you actually got work done...
   [E][X] team meeting (from: Mon 2pm to: 3pm)
____________________________________________________________
____________________________________________________________
I can't believe you lied to me...
   [E][ ] team meeting (from: Mon 2pm to: 3pm)
____________________________________________________________
____________________________________________________________
Here are the task(s) in your list:
1. [D][X] submit report (by: Friday)
2. [E][ ] team meeting (from: Mon 2pm to: 3pm)
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```

## Test case 26: Ignore surrounding and repeated whitespace

**Aim:** Verify that leading, trailing, and repeated whitespace around commands does not change the command or stored task description.

**Command:** `java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" -cp out Cookie`

**Inputs:**
```text
  todo   buy milk  
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
You have 1 task(s) now. Better start working.
____________________________________________________________
____________________________________________________________
Here are the task(s) in your list:
1. [T][ ] buy milk
____________________________________________________________
____________________________________________________________
Bye. I'm going to sleep.
____________________________________________________________
```
