# 📸 Full Lecturer System 

## 🔐 Login
<img src="images/login.png" width="700">
Allows users to securely log in based on their role (Admin, Lecturer, Student, Academic Leader).
Input validation ensures correct credentials.


---
## 👨‍🏫 Lecturer Dashboard
<img src="images/lecturer_dashboard.png" width="700">
Main navigation hub for lecturers, providing access to modules, classes, assessments, grading, and analytics.

---
## 👨‍🏫 Module page
<img src="images/modules.png" width="700">
Displays modules assigned to the lecturer. Users can navigate to related classes and assessments by clicking on the module card.

---
## 👨‍🏫 Class page
<img src="images/classes.png" width="700">
Shows classes under each module. Lecturers can select a class card to manage assessments and student records.

---
## 👨‍🏫 Assessments page
<img src="images/assessments.png" width="700">
Lists all assessments for a selected module or class, including weightage and type. Lecturers can delete assessments.

**Validation:**
- Assessments can only be deleted if no student has been graded yet


### Assessment - Design Assessment page
<img src="images/assessments_design.png" width="700">
Allows lecturers to create assessment components.

**Validation:**

- Total weightage must not exceed 100% (1.0)
- Final Exam can only be created once per class
- Full mark must be between 0–100

---
## 👨‍🏫 Grading page
<img src="images/grading.png" width="700">
Enables lecturers to input and manage student marks for each assessment.

**Validation:**

- Marking can only begin after total assessment weightage reaches 100%

### Grading - View Overall Student Result page
<img src="images/grading_resultclass.png" width="700">
<img src="images/grading_resultmodule.png" width="700">
Displays aggregated student performance at class and module levels.

### Grading - Marking page
<img src="images/grading_marking.png" width="700">
Supports mark entry with automatic weighted score calculation and feedback.

**Validation:**

- Marks entered must be between 0 and the defined full mark
- Supports mark entry with automatic weighted score calculation and feedback.


---
## 👨‍🏫 Analytics page
<img src="images/analytics.png" width="700">
Provides performance insights and summary statistics to help lecturers evaluate student outcomes.

---
## 👨‍🏫 Profile page
<img src="images/profile.png" width="700">
Displays lecturer personal information and account details.

### Profile - Update Profile page
<img src="images/profile_update profile.png" width="700">
Allows users to update personal information.

**Validation:**

- Username must be unique
- Email must be a valid Gmail, Yahoo, or Outlook account
- Phone number must follow Malaysian format (+60) with 10–11 digits
- Users must enter their existing password to validate the changes

### Profile - Change Password page
<img src="images/profile_changepassword.png" width="700">
Ensures secure password updates.

**Validation:**

- Current password must match existing password
- New password must be strong (≥ 8 characters, includes uppercase, lowercase, symbol, and number)

---
## ⭐ Additional Features:
### 🔍 Filter 
#### Before:
<img src="images/filter1.png" width="700">

#### After:
<img src="images/filter2.png" width="700">

### ✅ Validations 
<img src="images/validation1.png" width="700">
<img src="images/validation2.png" width="700">
<img src="images/validation3.png" width="700">
<img src="images/validation4.png" width="700">
<img src="images/validation5.png" width="700">
<img src="images/validation6.png" width="700">
