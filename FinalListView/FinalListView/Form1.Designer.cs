namespace FinalListView
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.Updatebutton = new System.Windows.Forms.Button();
            this.Cancelbutton = new System.Windows.Forms.Button();
            this.Savebutton = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.StudentNametextBox = new System.Windows.Forms.TextBox();
            this.ContactNumbertextBox = new System.Windows.Forms.TextBox();
            this.EmailAddresstextBox = new System.Windows.Forms.TextBox();
            this.StudentlistView = new System.Windows.Forms.ListView();
            this.columnHeader1 = new System.Windows.Forms.ColumnHeader();
            this.columnHeader2 = new System.Windows.Forms.ColumnHeader();
            this.columnHeader3 = new System.Windows.Forms.ColumnHeader();
            this.SuspendLayout();
            // 
            // Updatebutton
            // 
            this.Updatebutton.Location = new System.Drawing.Point(259, 188);
            this.Updatebutton.Name = "Updatebutton";
            this.Updatebutton.Size = new System.Drawing.Size(75, 23);
            this.Updatebutton.TabIndex = 0;
            this.Updatebutton.Text = "UPDATE";
            this.Updatebutton.UseVisualStyleBackColor = true;
            this.Updatebutton.Click += new System.EventHandler(this.Updatebutton_Click);
            // 
            // Cancelbutton
            // 
            this.Cancelbutton.Location = new System.Drawing.Point(352, 188);
            this.Cancelbutton.Name = "Cancelbutton";
            this.Cancelbutton.Size = new System.Drawing.Size(75, 23);
            this.Cancelbutton.TabIndex = 1;
            this.Cancelbutton.Text = "CANCEL";
            this.Cancelbutton.UseVisualStyleBackColor = true;
            this.Cancelbutton.Click += new System.EventHandler(this.Cancelbutton_Click);
            // 
            // Savebutton
            // 
            this.Savebutton.Location = new System.Drawing.Point(454, 188);
            this.Savebutton.Name = "Savebutton";
            this.Savebutton.Size = new System.Drawing.Size(75, 23);
            this.Savebutton.TabIndex = 2;
            this.Savebutton.Text = "SAVE";
            this.Savebutton.UseVisualStyleBackColor = true;
            this.Savebutton.Click += new System.EventHandler(this.Savebutton_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(49, 48);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(75, 13);
            this.label1.TabIndex = 3;
            this.label1.Text = "Student Name";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(52, 93);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(84, 13);
            this.label2.TabIndex = 4;
            this.label2.Text = "Contact Number";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(52, 129);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(73, 13);
            this.label3.TabIndex = 5;
            this.label3.Text = "Email Address";
            // 
            // StudentNametextBox
            // 
            this.StudentNametextBox.Location = new System.Drawing.Point(181, 48);
            this.StudentNametextBox.Name = "StudentNametextBox";
            this.StudentNametextBox.Size = new System.Drawing.Size(206, 20);
            this.StudentNametextBox.TabIndex = 6;
            // 
            // ContactNumbertextBox
            // 
            this.ContactNumbertextBox.Location = new System.Drawing.Point(181, 93);
            this.ContactNumbertextBox.Name = "ContactNumbertextBox";
            this.ContactNumbertextBox.Size = new System.Drawing.Size(206, 20);
            this.ContactNumbertextBox.TabIndex = 7;
            // 
            // EmailAddresstextBox
            // 
            this.EmailAddresstextBox.Location = new System.Drawing.Point(181, 129);
            this.EmailAddresstextBox.Name = "EmailAddresstextBox";
            this.EmailAddresstextBox.Size = new System.Drawing.Size(206, 20);
            this.EmailAddresstextBox.TabIndex = 8;
            // 
            // StudentlistView
            // 
            this.StudentlistView.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(255)))), ((int)(((byte)(224)))), ((int)(((byte)(192)))));
            this.StudentlistView.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader1,
            this.columnHeader2,
            this.columnHeader3});
            this.StudentlistView.GridLines = true;
            this.StudentlistView.Location = new System.Drawing.Point(37, 239);
            this.StudentlistView.Name = "StudentlistView";
            this.StudentlistView.Size = new System.Drawing.Size(531, 149);
            this.StudentlistView.TabIndex = 9;
            this.StudentlistView.UseCompatibleStateImageBehavior = false;
            this.StudentlistView.View = System.Windows.Forms.View.Details;
            this.StudentlistView.DoubleClick += new System.EventHandler(this.StudentlistView_DoubleClick);
            // 
            // columnHeader1
            // 
            this.columnHeader1.Text = "Student Name";
            this.columnHeader1.Width = 180;
            // 
            // columnHeader2
            // 
            this.columnHeader2.Text = "Contact Number";
            this.columnHeader2.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            this.columnHeader2.Width = 150;
            // 
            // columnHeader3
            // 
            this.columnHeader3.Text = "Email Address";
            this.columnHeader3.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            this.columnHeader3.Width = 200;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.ActiveCaption;
            this.ClientSize = new System.Drawing.Size(596, 400);
            this.Controls.Add(this.StudentlistView);
            this.Controls.Add(this.EmailAddresstextBox);
            this.Controls.Add(this.ContactNumbertextBox);
            this.Controls.Add(this.StudentNametextBox);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.Savebutton);
            this.Controls.Add(this.Cancelbutton);
            this.Controls.Add(this.Updatebutton);
            this.Name = "Form1";
            this.Text = "Student Form";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button Updatebutton;
        private System.Windows.Forms.Button Cancelbutton;
        private System.Windows.Forms.Button Savebutton;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox StudentNametextBox;
        private System.Windows.Forms.TextBox ContactNumbertextBox;
        private System.Windows.Forms.TextBox EmailAddresstextBox;
        private System.Windows.Forms.ListView StudentlistView;
        private System.Windows.Forms.ColumnHeader columnHeader1;
        private System.Windows.Forms.ColumnHeader columnHeader2;
        private System.Windows.Forms.ColumnHeader columnHeader3;
    }
}

