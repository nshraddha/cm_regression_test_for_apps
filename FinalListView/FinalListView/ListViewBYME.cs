using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace FinalListView
{
    public partial class Form1 : Form
    {
        private string studentName;
        private string contactNumber;
        private string emailAddress;
        private Student selectedStudent;
        private int selectedRowIndex;


        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            Savebutton.Visible = true;
            Cancelbutton.Visible = false;
            Updatebutton.Visible = false;
        }

        private void Savebutton_Click(object sender, EventArgs e)
        {
            
            studentName = StudentNametextBox.Text;
            
            contactNumber = ContactNumbertextBox.Text;
            
            emailAddress = EmailAddresstextBox.Text;

            Student studentObj = new Student( StudentNametextBox.Text, ContactNumbertextBox.Text , EmailAddresstextBox.Text);

            ListViewItem item = new ListViewItem();
            item.Tag = studentObj;
            item.Text = studentObj.StudentName_;
            item.SubItems.Add(studentObj.ContactNumber_);
            item.SubItems.Add(studentObj.EmailAddress_);
            StudentlistView.Items.Add(item);

            ClearTextBox();


        }

        private void ClearTextBox()
        {
            StudentNametextBox.Text = "";
            ContactNumbertextBox.Text = "";
            EmailAddresstextBox.Text = "";
        }

        private void StudentlistView_DoubleClick(object sender, EventArgs e)
        {
            ListViewItem selectedListViewCell = StudentlistView.SelectedItems[0];
            selectedStudent = (Student)selectedListViewCell.Tag;
            SetDataInTextBoxes();

            selectedRowIndex = StudentlistView.SelectedIndices[0];


            Savebutton.Visible = false;
            Cancelbutton.Visible = true;
            Updatebutton.Visible = true;

        }

        private void SetDataInTextBoxes()
        {
            StudentNametextBox.Text = selectedStudent.StudentName_;
            ContactNumbertextBox.Text = selectedStudent.ContactNumber_;
            EmailAddresstextBox.Text = selectedStudent.EmailAddress_;
        }

        private void Updatebutton_Click(object sender, EventArgs e)
        {
            Student StudentObj = new Student(StudentNametextBox.Text, ContactNumbertextBox.Text, EmailAddresstextBox.Text);

            ListViewItem item = new ListViewItem();
            item.Tag = StudentObj;
            item.Text = StudentObj.StudentName_;
            item.SubItems.Add(StudentObj.ContactNumber_);
            item.SubItems.Add(StudentObj.EmailAddress_);
            StudentlistView.Items[selectedRowIndex] = item;

            MakeOperationForSaveButton();
           
        }

        private void MakeOperationForSaveButton()
        {
            Savebutton.Visible = true;
            Cancelbutton.Visible = false;
            Updatebutton.Visible = false;
        }

        private void Cancelbutton_Click(object sender, EventArgs e)
        {
            foreach ( ListViewItem selctedItem in StudentlistView.SelectedItems)
            {
               StudentlistView.Items.Remove(selctedItem);
                
            }
            MakeOperationForSaveButton();
        }
    }
}
