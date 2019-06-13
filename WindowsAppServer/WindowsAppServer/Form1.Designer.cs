namespace WindowsAppServer
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
            this.connectButton = new System.Windows.Forms.Button();
            this.KeyControlText = new System.Windows.Forms.TextBox();
            this.checkBox = new System.Windows.Forms.CheckBox();
            this.label1 = new System.Windows.Forms.Label();
            this.exitButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // connectButton
            // 
            this.connectButton.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.connectButton.Location = new System.Drawing.Point(44, 108);
            this.connectButton.Name = "connectButton";
            this.connectButton.Size = new System.Drawing.Size(86, 32);
            this.connectButton.TabIndex = 0;
            this.connectButton.Text = "Connect";
            this.connectButton.UseVisualStyleBackColor = true;
            this.connectButton.Click += new System.EventHandler(this.Connect_Click);
            // 
            // KeyControlText
            // 
            this.KeyControlText.Location = new System.Drawing.Point(44, 52);
            this.KeyControlText.Name = "KeyControlText";
            this.KeyControlText.Size = new System.Drawing.Size(86, 20);
            this.KeyControlText.TabIndex = 1;
            this.KeyControlText.TextChanged += new System.EventHandler(this.KeyControlText_TextChanged);
            // 
            // checkBox
            // 
            this.checkBox.AutoCheck = false;
            this.checkBox.AutoSize = true;
            this.checkBox.Checked = true;
            this.checkBox.CheckState = System.Windows.Forms.CheckState.Checked;
            this.checkBox.ForeColor = System.Drawing.Color.CornflowerBlue;
            this.checkBox.Location = new System.Drawing.Point(155, 52);
            this.checkBox.Name = "checkBox";
            this.checkBox.Size = new System.Drawing.Size(88, 17);
            this.checkBox.TabIndex = 5;
            this.checkBox.Text = "AutoConnect";
            this.checkBox.UseVisualStyleBackColor = true;
            this.checkBox.CheckedChanged += new System.EventHandler(this.checkBox_CheckedChanged);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.ForeColor = System.Drawing.Color.CornflowerBlue;
            this.label1.Location = new System.Drawing.Point(41, 27);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(25, 13);
            this.label1.TabIndex = 6;
            this.label1.Text = "Key";
            // 
            // exitButton
            // 
            this.exitButton.Location = new System.Drawing.Point(168, 113);
            this.exitButton.Name = "exitButton";
            this.exitButton.Size = new System.Drawing.Size(75, 23);
            this.exitButton.TabIndex = 7;
            this.exitButton.Text = "Exit";
            this.exitButton.UseVisualStyleBackColor = true;
            this.exitButton.Click += new System.EventHandler(this.exitButton_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(255, 175);
            this.Controls.Add(this.exitButton);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.KeyControlText);
            this.Controls.Add(this.checkBox);
            this.Controls.Add(this.connectButton);
            this.Name = "Form1";
            this.Text = "ConnectToPhone";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button connectButton;
        private System.Windows.Forms.TextBox KeyControlText;
        private System.Windows.Forms.CheckBox checkBox;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button exitButton;
    }
}

