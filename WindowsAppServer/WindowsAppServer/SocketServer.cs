using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Windows.Forms;


namespace WindowsAppServer
{

    // State object for reading client data asynchronously  
    public class StateObject
    {
        // Client  socket.  
        public Socket workSocket = null;
        // Size of receive buffer.  
        public const int BufferSize = 1024;
        // Receive buffer.  
        public byte[] buffer = new byte[BufferSize];
        // Received data string.  
        public StringBuilder sb = new StringBuilder();
    }

    

    public class AsynchronousSocketListener
    {

      

        // Thread signal.  
        public static ManualResetEvent allDone = new ManualResetEvent(false);
        private static int myProt = 7070; // ben
        public Socket listener;
        private string computerIP = "";
        public string textControlKey;
        public static string data = null;

        private  bool checkflag;

        public AsynchronousSocketListener(bool checkCont,string textKey)
        {
            myProt = 7070;
            checkflag = checkCont;
            Ipfinder();
            textControlKey = textKey;
        }

        public void Ipfinder()
        {
            var host = Dns.GetHostEntry(Dns.GetHostName());
            foreach (var ipp in host.AddressList)
            {
                if (ipp.AddressFamily == AddressFamily.InterNetwork)
                {
                    Console.WriteLine("Waiting ip finder----------... " + ipp.ToString());
                    computerIP = ipp.ToString();
                }
            }
        }

        public void StartListening()
        {
            var host = Dns.GetHostEntry(Dns.GetHostName());
            foreach (var ipp in host.AddressList)
            {
                if (ipp.AddressFamily == AddressFamily.InterNetwork)
                {
                    Console.WriteLine("Waiti----------... " + ipp.ToString());
                }
            }

            IPAddress ip = IPAddress.Parse("192.168.1.25");

            // Establish the local endpoint for the socket.  
            // The DNS name of the computer  
            // running the listener is "host.contoso.com".  
            IPHostEntry ipHostInfo = Dns.GetHostEntry(Dns.GetHostName());
            IPAddress ipAddress = ipHostInfo.AddressList[0];
            IPEndPoint localEndPoint = new IPEndPoint(ipAddress, 6060);

            // Create a TCP/IP socket.  
            /*listener = new Socket(ipAddress.AddressFamily,
                SocketType.Stream, ProtocolType.Tcp);*/

            listener = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            // Bind the socket to the local endpoint and listen for incoming connections.  
            try
            {
                //listener.Bind(localEndPoint);
                listener.Bind(new IPEndPoint(ip, myProt));
                listener.Listen(10);

                while (true)
                {
                    // Set the event to nonsignaled state.  
                    allDone.Reset();

                    // Start an asynchronous socket to listen for connections.  
                    Console.WriteLine("Waiting for a connection...");
                    listener.BeginAccept(
                        new AsyncCallback(AcceptCallback),
                        listener);

                    // Wait until a connection is made before continuing.  
                    allDone.WaitOne();
                }

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }

            Console.WriteLine("\nPress ENTER to continue...");
            Console.Read();

        }

        public void AcceptCallback(IAsyncResult ar)
        {
            Console.WriteLine("\n AcceptCallback...");
            // Signal the main thread to continue.  
            allDone.Set();

            // Get the socket that handles the client request.  
            Socket listener = (Socket)ar.AsyncState;
            Socket handler = listener.EndAccept(ar);

            //woksocket ne alaka 
            Console.WriteLine("\n handler ::..." + handler.ToString());

            // Create the state object.  
            StateObject state = new StateObject();
            state.workSocket = handler;
            handler.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
                new AsyncCallback(ReadCallback), state);
        }

        public void ReadCallback(IAsyncResult ar)
        {
            Console.WriteLine("\n ReadCallback...");
            String content = String.Empty;

            // Retrieve the state object and the handler socket  
            // from the asynchronous state object.  
            StateObject state = (StateObject)ar.AsyncState;
            Socket handler = state.workSocket;

            // Read data from the client socket.   
            int bytesRead = handler.EndReceive(ar);
            Console.WriteLine("\n bytesRead  ...  " + bytesRead);
            if (bytesRead > 0)
            {
                // There  might be more data, so store the data received so far.  
                state.sb.Append(Encoding.ASCII.GetString(
                    state.buffer, 0, bytesRead));

                // Check for end-of-file tag. If it is not there, read   
                // more data.  
                content = state.sb.ToString();
                //Console.WriteLine("\n content  ...  " + content);// my comment
                
                if (content.IndexOf("p") > -1)
                {
                    // All the data has been read from the   
                    // client. Display it on the console.  
                    Console.WriteLine("Read {0} bytes from socket. \n Data : {1}",
                        content.Length, content);
                    //state.sb.Clear();
                    // Echo the data back to the client.  
                    //Send(handler, content);
                }
                else
                {
                    Console.WriteLine("\n bos cıktı haci  ...  ");
                    // Not all data received. Get more.  
                    handler.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
                    new AsyncCallback(ReadCallback), state);
                }
            }
        }

        public void Send(Socket handler, String data)
        {
            // Convert the string data to byte data using ASCII encoding.  
            byte[] byteData = Encoding.ASCII.GetBytes(data);

            // Begin sending the data to the remote device.  
            handler.BeginSend(byteData, 0, byteData.Length, 0,
                new AsyncCallback(SendCallback), handler);
        }

        private void SendCallback(IAsyncResult ar)
        {
            try
            {
                // Retrieve the socket from the state object.  
                Socket handler = (Socket)ar.AsyncState;

                // Complete sending the data to the remote device.  
                int bytesSent = handler.EndSend(ar);
                Console.WriteLine("Sent {0} bytes to client.", bytesSent);

                handler.Shutdown(SocketShutdown.Both);
                handler.Close();

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        public void Receive(Socket socket, byte[] buffer, int offset, int size, int timeout)
        {
            Console.WriteLine("receive data \n");
            int startTickCount = Environment.TickCount;
            int received = 0;  // how many bytes is already received
            do
            {
                if (Environment.TickCount > startTickCount + timeout)
                    throw new Exception("Timeout.");
                try
                {
                    received += socket.Receive(buffer, offset + received, size - received, SocketFlags.None);
                }
                catch (SocketException ex)
                {
                    if (ex.SocketErrorCode == SocketError.WouldBlock ||
                        ex.SocketErrorCode == SocketError.IOPending ||
                        ex.SocketErrorCode == SocketError.NoBufferSpaceAvailable)
                    {
                        // socket buffer is probably empty, wait and try again
                        Thread.Sleep(30);
                    }
                    else
                        throw ex;  // any serious error occurr
                }
            } while (received < size);
        }

        public void StartListening2()
        {

            short bVal = 50;
            int menuItem = 0;
            int preVal = 0;
            bool controlFlag = false;

            int preX=0,preY = 0;
            // Data buffer for incoming data.  
            byte[] bytes = new Byte[1024];

            Voice volControl = new Voice();
            WindowsBright brightControl = new WindowsBright();

            IPAddress ip = IPAddress.Parse(computerIP);
            // Establish the local endpoint for the socket.  
            // Dns.GetHostName returns the name of the   
            // host running the application.  
            IPHostEntry ipHostInfo = Dns.GetHostEntry(Dns.GetHostName());
            IPAddress ipAddress = ipHostInfo.AddressList[0];
            IPEndPoint localEndPoint = new IPEndPoint(ipAddress, myProt);

            // Create a TCP/IP socket.  
            Socket listener = new Socket(AddressFamily.InterNetwork,
                SocketType.Stream, ProtocolType.Tcp);

            // Bind the socket to the local endpoint and   
            // listen for incoming connections.  
            try
            {
                listener.Bind(new IPEndPoint(ip, myProt));
                listener.Listen(10);

                /*---------------------ip bulmak için ------------------------------------*/
                IPsending(listener);
                /*-------------------------------------------------------------------------*/

                /*---------------------key control ------------------------------------*/
                if (checkflag == false)
                {                  
                    Console.WriteLine("Key Kontrol");
                    KeyControl(listener);
                }
                /*-------------------------------------------------------------------------*/

                /*---------------------brightness control ----------------------------------*/
                BrightSending(listener);
                /*-------------------------------------------------------------------------*/

                // Start listening for connections.  
                while (true)
                {
                    Console.WriteLine("Waiting for a connection...");
                    // Program is suspended while waiting for an incoming connection.  
                    Socket handler = listener.Accept();
                    data = null;
                    Console.WriteLine("Connection is successss...");
                    // An incoming connection needs to be processed.


                    while (true)
                    {

                        int bytesRec = handler.Receive(bytes);
                        //Console.WriteLine("\n bytesRead  ...  " + bytesRec);

                        data += Encoding.ASCII.GetString(bytes, 0, bytesRec);
                        //Console.WriteLine("data  is ... {1}",data);
                        if (data.IndexOf('#') > -1)
                        {

                            break;
                        }
                    }

                    // Show the data on the console.  
                    Console.WriteLine("Text received : {0}", data);
                    string temp = data.Substring(4, data.Length - 5);
                    Console.WriteLine("Tmep nedir : {0}", temp);

                    if (temp.Contains("keyboard")) { menuItem = 1; controlFlag = false; }
                    else if (temp.Contains("bright")) { menuItem = 2; controlFlag = false; }
                    else if (temp.Contains("voice")) { menuItem = 3; controlFlag = false; }
                    else if (temp.Contains("mouse")) { menuItem = 4; controlFlag = true; }

                    if (controlFlag == true)
                    { 
                        if (menuItem == 1)
                        {
                            try
                            {
                                int m = Int32.Parse(temp);
                                bVal = (short)m;
                                Console.WriteLine("Convert to int text is : {0}", m);
                            }
                            catch (FormatException e)
                            {
                                Console.WriteLine(e.Message);
                            }

                            Keyboard.Keystroke(bVal);

                        }
                        else if (menuItem == 2)
                        {
                            try
                            {
                                int m = Int32.Parse(temp);
                                bVal = (short)m;
                                Console.WriteLine("Convert to int text is : {0}", m);
                            }
                            catch (FormatException e)
                            {
                                Console.WriteLine(e.Message);
                            }

                            brightControl.startup_brightness(bVal);
                        }
                        else if (menuItem == 3)
                        {
                            try
                            {
                                int m = Int32.Parse(temp);
                                bVal = (short)m;
                                Console.WriteLine("Convert to int text is : {0}", m);
                            }
                            catch (FormatException e)
                            {
                                Console.WriteLine(e.Message);
                            }

                            if (bVal > preVal)
                                for (int i = 0; i < 5; i++)
                                    volControl.btnIncVol_Click();
                            else
                                for (int i = 0; i < 5; i++)
                                    volControl.btnDecVol_Click();

                            preVal = bVal;
                        }
                        else if (menuItem == 4)
                        {
                            int tire = temp.IndexOf('-');                         
                            string posx = temp.Substring(5, tire-5);
                            string posy = temp.Substring(tire + 1, temp.Length-(tire+1));
                            Console.WriteLine(posx + "     " + posy);
                            int posXInt = int.Parse(posx);
                            int posYInt = int.Parse(posy);
                            Console.WriteLine(posXInt + "     " + posYInt);

                            int netX = (posXInt - preX);
                            int netY = (posYInt - preY);

                            MouseManipulator.VirtualMouse.Move(netX, netY);
                            preX = posXInt;
                            preY = posYInt;
                        }

                    }
                    //handler.Shutdown(SocketShutdown.Both);
                    //handler.Close();

                    controlFlag = true;
                }

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }

            Console.WriteLine("\nPress ENTER to continue...");
            Console.Read();

        }

        public void KeyControl(Socket listener)
        {
            Console.WriteLine("Waiting for a KeyControl connection...");
            // Program is suspended while waiting for an incoming connection.  
            Socket sendhandler = listener.Accept();
            data = null;
            Console.WriteLine("Connection KeyControl is successss...");
            Send(sendhandler, textControlKey);
            //System.Threading.Thread.Sleep(100);
            checkflag = true;
        }
        public void BrightSending(Socket listener)
        {
            int computerBrightness=0;
            Console.WriteLine("Waiting for a BrightSending connection...");
            // Program is suspended while waiting for an incoming connection.  
            Socket sendhandler = listener.Accept();
            data = null;
            Console.WriteLine("Connection BrightSending is successss...");
            computerBrightness=WindowsBright.GetBrightness();
            string strBright = ""+computerBrightness;
            Send(sendhandler, strBright);
            //System.Threading.Thread.Sleep(100);
            checkflag = true;
        }
        public void IPsending(Socket listener)
        {
            Console.WriteLine("Waiting for a IPsending connection...");
            // Program is suspended while waiting for an incoming connection.  
            Socket iphandler = listener.Accept();
            data = null;
            Console.WriteLine("Connection IPsending is successss...");
            iphandler.Shutdown(SocketShutdown.Both);
            iphandler.Close();
        }

        public String convertBytes2String(byte[] b)
        {
            int len = b.Length;
            if (len == 0) return "Bytes Length is ZERO!";
            StringBuilder sb = new StringBuilder(len * 3);
            for (int i = 0; i < len; ++i)
            {
                sb.Append(String.Format("%02x ", b[i]));
            }
            return sb.ToString();

        }

    }
}
