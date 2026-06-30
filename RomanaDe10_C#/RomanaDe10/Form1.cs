using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace RomanaDe10
{
    public partial class Form1 : Form
    {
        int totalIntrebari = 9;
        int intrebariCorecte = 0;
        int intrebariGresite = 0;
        int intrebareCurenta = 0;
        int pozitieVarC = 0;

        public Form1()
        {
            InitializeComponent();
            SetUniversalFont();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            // TODO: This line of code loads data into the 'bdDataSet.Utilizator' table. You can move, or remove it, as needed.
            this.utilizatorTableAdapter.Fill(this.bdDataSet.Utilizator);
            // TODO: This line of code loads data into the 'bdDataSet.Progres' table. You can move, or remove it, as needed.
            this.progresTableAdapter.Fill(this.bdDataSet.Progres);
            // TODO: This line of code loads data into the 'bdDataSet.Intrebari' table. You can move, or remove it, as needed.
            //this.intrebariTableAdapter.Fill(this.bdDataSet.Intrebari);
            bdDataSet.EnforceConstraints = false;
        }

        /////////////////////////////////
        // VERIFICARE PAROLA PUTERNICA //
        /////////////////////////////////

        private bool IsValidPassword(string password)
        {
            int minLength = 8;
            bool containsUpperCase = false;
            bool containsLowerCase = false;
            bool containsDigit = false;
            bool containsSpecialChar = false;
            if (password.Length < minLength)
                return false;
            foreach (char c in password)
            {
                if (char.IsUpper(c))
                    containsUpperCase = true;
                else if (char.IsLower(c))
                    containsLowerCase = true;
                else if (char.IsDigit(c))
                    containsDigit = true;
                else if (char.IsSymbol(c) || char.IsPunctuation(c))
                    containsSpecialChar = true;
            }
            return containsUpperCase && containsLowerCase && containsDigit && containsSpecialChar;
        }

        private void button1_Click_1(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage2;
            textBox1.Clear();
            textBox2.Clear();
            textBox3.Clear();
            textBox4.Clear();
        }

        /////////////////
        // CREARE CONT //
        /////////////////

        private void button3_Click(object sender, EventArgs e)
        {
            textBox5.Clear();
            textBox6.Clear();
            label10.Text = "";
            string nume = textBox1.Text.Trim();
            string prenume = textBox2.Text.Trim();
            string utilizator = textBox3.Text.Trim();
            string parola = textBox4.Text.Trim();
            if (nume.Length > 0 && prenume.Length > 0 && utilizator.Length > 0 && parola.Length > 0)
            {
                utilizatorTableAdapter.Fill(bdDataSet.Utilizator);
                DataTable d = bdDataSet.Utilizator;
                bool ok = true;
                for (int i = 0; i < d.Rows.Count; i++)
                {
                    if (d.Rows[i]["username"].ToString() == utilizator)
                    {
                        ok = false;
                        label10.Text += "Username existent! \n";
                    }
                }
                if (IsValidPassword(parola) == false)
                {
                    ok = false;
                    label10.Text += "Parola este prea slaba; aceasta trebuie sa contina litere mari, mici, cifre si caractere speciale!\n";
                }
                if (ok)
                {
                    utilizatorTableAdapter.Adaugare(nume, prenume, utilizator, parola);
                    textBox1.Text = "";
                    textBox2.Text = "";
                    textBox3.Text = "";
                    textBox4.Text = "";
                    MessageBox.Show("Contul a fost creat cu succes!");
                    tabControl1.SelectedTab = tabPage3;
                }
                else
                {
                    if(label10.Text=="")
                        label10.Text += "Date invalide!";
                }
            }

        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox3_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox4_TextChanged(object sender, EventArgs e)
        {

        }

        private void button2_Click_1(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage3;
            textBox5.Clear();
            textBox6.Clear();
        }

        ///////////////
        // CONECTARE //
        ///////////////

        private void button4_Click_1(object sender, EventArgs e)
        {
            string utilizator = textBox5.Text.Trim();
            string parola = textBox6.Text.Trim();
            if (utilizator.Length > 0 && parola.Length >= 0)
            {
                utilizatorTableAdapter.Fill(bdDataSet.Utilizator);
                DataTable d = bdDataSet.Utilizator;
                bool ok = false;
                for (int i = 0; i < d.Rows.Count; i++)
                {
                    if (d.Rows[i]["username"].ToString() == utilizator && d.Rows[i]["parola"].ToString() == parola)
                        ok = true;
                }
                if (ok)
                {
                    CurrentUser currentuser = new CurrentUser(utilizator);
                    MessageBox.Show("Conectare cu succes!");
                    tabControl1.SelectedTab = tabPage4;
                }
                else
                {
                    MessageBox.Show("Username sau parola gresite!");
                }
            }
            else
            {
                MessageBox.Show("Date invalide!");
            }
        }

        private void textBox5_TextChanged(object sender, EventArgs e)
        {
            
        }

        private void textBox6_TextChanged(object sender, EventArgs e)
        {

        }

        private void button6_Click(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage5;
        }

        private void button9_Click(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage1;
        }

        private void button8_Click(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage4;
        }

        private void button10_Click(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage5;
        }

        ////////////////////////////////////////
        // GENERARE QUIZ PENTRU FIECARE OPERA //
        ////////////////////////////////////////

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            label26.Text = "";
            tabControl1.SelectedTab = tabPage6;
            label17.Text = "";
            label18.Text = "";
            label19.Text = "";
            initializeazaTest(comboBox1.SelectedItem.ToString());
            label26.Text += comboBox1.SelectedItem.ToString();
        }

        private void afiseazaIntrebarea(string cerinta, string v1, string v2, string v3, string v4)
        {
            label20.Text = cerinta;
            radioButton1.Text = v1;
            radioButton2.Text = v2;
            radioButton3.Text = v3;
            radioButton4.Text = v4;
        }
        private void initializeazaTest(string opera)
        {
            button11.Text = "CONTINUARE";
            totalIntrebari = 9;
            intrebariCorecte = 0;
            intrebariGresite = 0;
            afiseazaScoruri();
            intrebariTableAdapter.IntrebariPeOpera(bdDataSet.Intrebari,opera);
            DataTable d = bdDataSet.Intrebari;
            intrebareCurenta = 0;
            int numarIntrebare = intrebareCurenta;
            intrebareCurenta++;
            string vc = d.Rows[numarIntrebare]["vc"].ToString();
            string cerinta = d.Rows[numarIntrebare]["cerinta"].ToString();
            string v1 = d.Rows[numarIntrebare]["v1"].ToString();
            string v2 = d.Rows[numarIntrebare]["v2"].ToString();
            string v3 = d.Rows[numarIntrebare]["v3"].ToString();
            Random r = new Random();
            int pozitieRaspunsCorect = r.Next(1, 4);
            pozitieVarC = pozitieRaspunsCorect;
            if (pozitieRaspunsCorect == 1)
            {
                afiseazaIntrebarea(cerinta, vc, v1, v2, v3);
            }
            else
            {
                if (pozitieRaspunsCorect == 2)
                {
                    afiseazaIntrebarea(cerinta, v1, vc, v2, v3);
                }
                else
                {
                    if (pozitieRaspunsCorect == 3)
                    {
                        afiseazaIntrebarea(cerinta, v1, v2, vc, v3);
                    }
                    else
                    {
                        if (pozitieRaspunsCorect == 4)
                        {
                            afiseazaIntrebarea(cerinta, v1, v2, v3, vc);
                        }
                    }
                }
            }
        }

        //////////////////
        // AFISARE SCOR //
        //////////////////

        private void afiseazaScoruri()
        {
            label17.Text = "Răspunsuri corecte: " + intrebariCorecte.ToString();
            label18.Text = "Răspunsuri greșite: " + intrebariGresite.ToString();
            label19.Text = "Întrebări rămase: " + totalIntrebari.ToString();
        }

        //////////////////////
        // ACTUALIZARE SCOR //
        //////////////////////

        private void actualizeazaScor(bool eRaspunsulCorect)
        {
            totalIntrebari = totalIntrebari - 1;

            if (totalIntrebari == 0)
            {
                button11.Text = "Finalizeaza test";
            }

            if (eRaspunsulCorect == true)
            {
                intrebariCorecte++;
            }
            else
            {
                intrebariGresite++;
            }

            afiseazaScoruri();

        }

        ///////////////////////////////////
        // GENERARE URMATOAREA INTREBARE //
        ///////////////////////////////////

        private void button11_Click(object sender, EventArgs e)
        {
            intrebariTableAdapter.IntrebariPeOpera(bdDataSet.Intrebari, comboBox1.SelectedItem.ToString());
            DataTable d = bdDataSet.Intrebari;
            int totalCorecteNow = intrebariCorecte;
            if (pozitieVarC == 1) 
            { 
                if (radioButton1.Checked) 
                    actualizeazaScor(true); 
            }
            if (pozitieVarC == 2) 
            { 
                if (radioButton2.Checked) 
                    actualizeazaScor(true); 
            }
            if (pozitieVarC == 3) 
            { 
                if (radioButton3.Checked) 
                    actualizeazaScor(true); 
            }
            if (pozitieVarC == 4) 
            { 
                if (radioButton4.Checked) 
                    actualizeazaScor(true); 
            }
            if (totalCorecteNow == intrebariCorecte)
            {
                actualizeazaScor(false);
            }
            Random r = new Random();
            int numarIntrebare;
            numarIntrebare=intrebareCurenta;
            intrebareCurenta++;
            int pozitieRaspunsCorect = r.Next(1, 4);
            pozitieVarC = pozitieRaspunsCorect;
            if (totalIntrebari > -1)
            {
                string vc = d.Rows[numarIntrebare]["vc"].ToString();
                string cerinta = d.Rows[numarIntrebare]["cerinta"].ToString();
                string v1 = d.Rows[numarIntrebare]["v1"].ToString();
                string v2 = d.Rows[numarIntrebare]["v2"].ToString();
                string v3 = d.Rows[numarIntrebare]["v3"].ToString();
                if (pozitieRaspunsCorect == 1)
                {
                    afiseazaIntrebarea(cerinta, vc, v1, v2, v3);
                }
                else
                {
                    if (pozitieRaspunsCorect == 2)
                    {
                        afiseazaIntrebarea(cerinta, v1, vc, v2, v3);
                    }
                    else
                    {
                        if (pozitieRaspunsCorect == 3)
                        {
                            afiseazaIntrebarea(cerinta, v1, v2, vc, v3);
                        }
                        else
                        {
                            if (pozitieRaspunsCorect == 4)
                            {
                                afiseazaIntrebarea(cerinta, v1, v2, v3, vc);
                            }
                        }
                    }
                }
            }
            else
            {
                intrebariCorecte = intrebariCorecte * 10;
                label21.Text = "SCORUL TAU: " + intrebariCorecte.ToString() + "/100";
                string scor=intrebariCorecte.ToString() + "/100";
                tabControl1.SelectedTab = tabPage7;
                DateTime data = DateTime.Now;
                progresTableAdapter.InsertQuery(CurrentUser.Curent, comboBox1.SelectedItem.ToString(), scor, data);
            }
            radioButton1.Checked = false;
            radioButton2.Checked = false;
            radioButton3.Checked = false;
            radioButton4.Checked = false;
        }

        private void button12_Click(object sender, EventArgs e)
        {
            textBox8.Clear();
            textBox9.Clear();
            textBox10.Clear();
            tabControl1.SelectedTab = tabPage8;
            Progres();
        }

        private void button7_Click(object sender, EventArgs e)
        {
            textBox8.Clear();
            textBox9.Clear();
            textBox10.Clear();
            tabControl1.SelectedTab = tabPage8;
            Progres();
        }

        /////////////
        // PROGRES //
        /////////////

        private void Progres()
        {
            progresTableAdapter.Ordonare(bdDataSet.Progres, CurrentUser.Curent);
            DataTable p = bdDataSet.Progres;
            for (int i = 0; i < p.Rows.Count; i++)
            {
                textBox8.Text +=  p.Rows[i]["opera"] + Environment.NewLine;
                textBox9.Text += p.Rows[i]["scor"] + Environment.NewLine;
                textBox10.Text += p.Rows[i]["data"] + Environment.NewLine;
            }
        }

        private void button13_Click(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage4;
        }

        private void button5_Click(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage9;
        }

        ////////////
        // ESEURI //
        ////////////

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage10;
            textBox7.Multiline = true;
            textBox7.ScrollBars = ScrollBars.Vertical;
            textBox7.Dock = DockStyle.Fill;
            /*
            string fisier=comboBox2.SelectedItem.ToString();
            // citire text din fisier
            string filePath = "H:\\RomanaDe10\\Eseuri\\"+fisier+".txt"; // calea fisierului
            if (File.Exists(filePath))
            {
                string fileContent = File.ReadAllText(filePath);
                textBox7.Text = fileContent;
            }
            else
            {
                textBox7.Text = "File not found or inaccessible.";
            }
             * */
            string selectedFile = comboBox2.SelectedItem.ToString();
            string fileContent = GetResourceFileContent(selectedFile);
            textBox7.Text = fileContent;
        }

        private string GetResourceFileContent(string fileName)
        {
            switch (fileName)
            {
                case "Alexandru Lăpușneanul":
                    return Properties.Resources.Alexandru_Lăpușneanul;
                case "Baltagul":
                    return Properties.Resources.Baltagul;
                case "Ion":
                    return Properties.Resources.Ion;
                case "Moara cu noroc":
                    return Properties.Resources.Moara_cu_noroc;
                case "Povestea lui Harap-Alb":
                    return Properties.Resources.Povestea_lui_Harap_Alb;
                case "Eu nu strivesc corola de minuni a lumii":
                    return Properties.Resources.Eu_nu_strivesc_corola_de_minuni_a_lumii;
                case "O scrisoare pierdută":
                    return Properties.Resources.O_scrisoare_pierdută;
                case "Ultima noapte de dragoste, întâia noapte de război":
                    return Properties.Resources.Ultima_noapte_de_dragoste__întâia_noapte_de_război;
                case "Floare albastră":
                    return Properties.Resources.Floare_albastră;
                case "Enigma Otiliei":
                    return Properties.Resources.Enigma_Otiliei;
                default:
                    return string.Empty;
            }
        }

        private void button16_Click(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage9;
        }

        private void button14_Click(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage5;
        }

        private void SetUniversalFont()
        {
            // Define the new font
            Font newFont = new Font("Impact", 14);

            // Apply the new font to the form and all child controls
            this.Font = newFont;

            // Optionally, you can iterate through all child controls
            SetFontForAllControls(this, newFont);
        }

        private void SetFontForAllControls(Control control, Font font)
        {
            control.Font = font;
            foreach (Control childControl in control.Controls)
            {
                if(control is Label && (control.Name!="label1" || control.Name!="label15"))
                    SetFontForAllControls(childControl, font);
            }
        }

        private void button15_Click(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage4;
        }

        private void button10_Click_1(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage1;
        }

        private void button17_Click(object sender, EventArgs e)
        {
            tabControl1.SelectedTab = tabPage1;
        }
    }
}