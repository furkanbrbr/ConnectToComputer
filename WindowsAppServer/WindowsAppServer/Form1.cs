using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Net;
using System.Net.Sockets;
using System.Collections;
using System.Threading;
using WindowsAppServer;
using System.Diagnostics;

namespace WindowsAppServer { 

  
    public partial class Form1 : Form
    {
        public static ManualResetEvent allDone = new ManualResetEvent(false);

        public static bool checkControl = true;
        public string keycont;

        public static void ThreadProc()
        {
            
            AsynchronousSocketListener objj = new AsynchronousSocketListener(checkControl, "12345");
            objj.StartListening2();
        }

        public Form1()
        {
            InitializeComponent();

        }     

        private void Connect_Click(object sender, EventArgs e)
        {

            WindowsBright winObj = new WindowsBright();

            //MouseManipulator.VirtualMouse.Move(200, 300);
            //System.Windows.Forms.Cursor.Position = new Point(-5, -5);

            Thread t = new Thread(new ThreadStart(ThreadProc));
            t.Start();
        }


        private void KeyControlText_TextChanged(object sender, EventArgs e)
        {
            keycont = KeyControlText.Text;
        }

        private void checkBox_CheckedChanged(object sender, EventArgs e)
        {
            if (checkBox.Checked)
            {
                checkControl = true;
            }
            else if (!checkBox.Checked)
            {
                checkControl = false;
            }
            
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            Console.WriteLine("sysysyysyysys");
            Process.GetCurrentProcess().Kill();
            Application.Exit();
        }
    }
  
}


