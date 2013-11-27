namespace FinalListView
{
    public class Student
    {
        private string StudentName;
        private string ContactNumber;
        private string EmailAddress;

        public Student(string studentName, string contactNumber, string emailAddress)
        {
            this.StudentName = studentName;
            this.ContactNumber = contactNumber;
            this.EmailAddress = emailAddress;
        }

        public string EmailAddress_
        {
            get { return EmailAddress; }
        }

        public string ContactNumber_
        {
            get { return ContactNumber; }
        }

        public string StudentName_
        {
            get { return StudentName; }
        }
    }
     

}
