package com.mtools.core.plugin.security;

class CEA
{

    long ExtendKey[][];
    int CEA_ROTATE;
    int CEA_MPI_LONG;

    public CEA()
    { 
        CEA_ROTATE = 6;
        CEA_MPI_LONG = 2;
        ExtendKey = new long[2 * CEA_ROTATE + 4][2];
    }

    protected void Add(long s1[], long s2[], long res[], int length)
    {
        long c = 0L;
        for(int i = length - 1; i >= 0; i--)
        {
            res[i] = s1[i] + s2[i] + c;
            c = (res[i] & 0xff00000000L) >>> 32;
            res[i] = res[i] & 0xffffffffL;
        }

    }

    public void ByteToInt(byte source[], int result[], int length)
    {
        for(int i = 0; i < length; i++)
            if(source[i] >= 0)
                result[i] = source[i];
            else
                result[i] = source[i] + 256;

    }

    public void CEADecrypt(byte cipher[], byte plain[])
    {
        long A[] = new long[2];
        long B[] = new long[2];
        long Temp1[] = new long[2];
        long Temp2[] = new long[2];
        int Temp[] = new int[16];
        ByteToInt(cipher, Temp, 16);
        A[0] = (long)Temp[0] << 24 | (long)Temp[1] << 16 | (long)Temp[2] << 8 | (long)Temp[3];
        A[1] = (long)Temp[4] << 24 | (long)Temp[5] << 16 | (long)Temp[6] << 8 | (long)Temp[7];
        B[0] = (long)Temp[8] << 24 | (long)Temp[9] << 16 | (long)Temp[10] << 8 | (long)Temp[11];
        B[1] = (long)Temp[12] << 24 | (long)Temp[13] << 16 | (long)Temp[14] << 8 | (long)Temp[15];
        Add(A, ExtendKey[0], A, CEA_MPI_LONG);
        Add(B, ExtendKey[1], B, CEA_MPI_LONG);
        for(int i = 1; i <= CEA_ROTATE; i++)
        {
            Xor(A, B, Temp1, CEA_MPI_LONG);
            int shift = (int)(B[1] & (long)63);
            LShift(Temp1, Temp2, shift, CEA_MPI_LONG);
            Add(Temp2, ExtendKey[2 * i], A, CEA_MPI_LONG);
            Xor(B, A, Temp1, CEA_MPI_LONG);
            shift = (int)(A[1] & (long)63);
            LShift(Temp1, Temp2, shift, CEA_MPI_LONG);
            Add(Temp2, ExtendKey[2 * i + 1], B, CEA_MPI_LONG);
        }

        Xor(A, ExtendKey[2 * CEA_ROTATE + 2], Temp1, CEA_MPI_LONG);
        Copy(Temp1, A, CEA_MPI_LONG);
        Xor(B, ExtendKey[2 * CEA_ROTATE + 3], Temp1, CEA_MPI_LONG);
        Copy(Temp1, B, CEA_MPI_LONG);
        LShift(A, Temp1, 1, CEA_MPI_LONG);
        LShift(B, Temp2, 1, CEA_MPI_LONG);
        long Bit1 = Temp1[1] & (long)1;
        long Bit2 = Temp2[1] & (long)1;
        Temp1[1] = Temp1[1] & 0xfffffffeL;
        Temp1[1] = Temp1[1] | Bit2;
        Temp2[1] = Temp2[1] & 0xfffffffeL;
        Temp2[1] = Temp2[1] | Bit1;
        Bit1 = Temp1[0] & 0x80000000L;
        Bit2 = (Temp1[0] & 0x40000000L) << 1;
        Bit1 ^= Bit2;
        Temp1[0] = Temp1[0] & 0x7fffffffL;
        Temp1[0] = Temp1[0] | Bit1;
        Copy(Temp1, A, CEA_MPI_LONG);
        Copy(Temp2, B, CEA_MPI_LONG);
        for(int i = CEA_ROTATE; i >= 1; i--)
        {
            Sub(B, ExtendKey[2 * i + 1], Temp1, CEA_MPI_LONG);
            int shift = (int)(A[1] & (long)63);
            RShift(Temp1, Temp2, shift, CEA_MPI_LONG);
            Xor(Temp2, A, B, CEA_MPI_LONG);
            Sub(A, ExtendKey[2 * i], Temp1, CEA_MPI_LONG);
            shift = (int)(B[1] & (long)63);
            RShift(Temp1, Temp2, shift, CEA_MPI_LONG);
            Xor(Temp2, B, A, CEA_MPI_LONG);
        }

        Sub(B, ExtendKey[1], Temp1, CEA_MPI_LONG);
        Temp[8] = (int)(Temp1[0] >>> 24 & (long)255);
        Temp[9] = (int)(Temp1[0] >>> 16 & (long)255);
        Temp[10] = (int)(Temp1[0] >>> 8 & (long)255);
        Temp[11] = (int)(Temp1[0] & (long)255);
        Temp[12] = (int)(Temp1[1] >>> 24 & (long)255);
        Temp[13] = (int)(Temp1[1] >>> 16 & (long)255);
        Temp[14] = (int)(Temp1[1] >>> 8 & (long)255);
        Temp[15] = (int)(Temp1[1] & (long)255);
        Sub(A, ExtendKey[0], Temp1, CEA_MPI_LONG);
        Temp[0] = (int)(Temp1[0] >>> 24 & (long)255);
        Temp[1] = (int)(Temp1[0] >>> 16 & (long)255);
        Temp[2] = (int)(Temp1[0] >>> 8 & (long)255);
        Temp[3] = (int)(Temp1[0] & (long)255);
        Temp[4] = (int)(Temp1[1] >>> 24 & (long)255);
        Temp[5] = (int)(Temp1[1] >>> 16 & (long)255);
        Temp[6] = (int)(Temp1[1] >>> 8 & (long)255);
        Temp[7] = (int)(Temp1[1] & (long)255);
        IntToByte(Temp, plain, 16);
    }

    public void CEAEncrypt(byte plain[], byte cipher[])
    {
        long A[] = new long[2];
        long B[] = new long[2];
        long Temp1[] = new long[2];
        long Temp2[] = new long[2];
        int Temp[] = new int[16];
        ByteToInt(plain, Temp, 16);
        A[0] = (long)Temp[0] << 24 | (long)Temp[1] << 16 | (long)Temp[2] << 8 | (long)Temp[3];
        A[1] = (long)Temp[4] << 24 | (long)Temp[5] << 16 | (long)Temp[6] << 8 | (long)Temp[7];
        B[0] = (long)Temp[8] << 24 | (long)Temp[9] << 16 | (long)Temp[10] << 8 | (long)Temp[11];
        B[1] = (long)Temp[12] << 24 | (long)Temp[13] << 16 | (long)Temp[14] << 8 | (long)Temp[15];
        Add(A, ExtendKey[0], A, CEA_MPI_LONG);
        Add(B, ExtendKey[1], B, CEA_MPI_LONG);
        for(int i = 1; i <= CEA_ROTATE; i++)
        {
            Xor(A, B, Temp1, CEA_MPI_LONG);
            int shift = (int)(B[1] & (long)63);
            LShift(Temp1, Temp2, shift, CEA_MPI_LONG);
            Add(Temp2, ExtendKey[2 * i], A, CEA_MPI_LONG);
            Xor(B, A, Temp1, CEA_MPI_LONG);
            shift = (int)(A[1] & (long)63);
            LShift(Temp1, Temp2, shift, CEA_MPI_LONG);
            Add(Temp2, ExtendKey[2 * i + 1], B, CEA_MPI_LONG);
        }

        LShift(A, Temp1, 63, CEA_MPI_LONG);
        LShift(B, Temp2, 63, CEA_MPI_LONG);
        long Bit1 = Temp1[0] & 0x80000000L;
        long Bit2 = Temp2[0] & 0x80000000L;
        Temp1[0] = Temp1[0] & 0x7fffffffL;
        Temp1[0] = Temp1[0] | Bit2;
        Temp2[0] = Temp2[0] & 0x7fffffffL;
        Temp2[0] = Temp2[0] | Bit1;
        Bit1 = Temp1[0] & 0x40000000L;
        Bit2 = (Temp1[0] & 0x20000000L) << 1;
        Bit1 ^= Bit2;
        Temp1[0] = Temp1[0] & 0xbfffffffL;
        Temp1[0] = Temp1[0] | Bit1;
        Xor(Temp1, ExtendKey[2 * CEA_ROTATE + 2], A, CEA_MPI_LONG);
        Xor(Temp2, ExtendKey[2 * CEA_ROTATE + 3], B, CEA_MPI_LONG);
        for(int i = CEA_ROTATE; i >= 1; i--)
        {
            Sub(B, ExtendKey[2 * i + 1], Temp1, CEA_MPI_LONG);
            int shift = (int)(A[1] & (long)63);
            RShift(Temp1, Temp2, shift, CEA_MPI_LONG);
            Xor(Temp2, A, B, CEA_MPI_LONG);
            Sub(A, ExtendKey[2 * i], Temp1, CEA_MPI_LONG);
            shift = (int)(B[1] & (long)63);
            RShift(Temp1, Temp2, shift, CEA_MPI_LONG);
            Xor(Temp2, B, A, CEA_MPI_LONG);
        }

        Sub(B, ExtendKey[1], Temp1, CEA_MPI_LONG);
        Temp[8] = (int)(Temp1[0] >>> 24 & (long)255);
        Temp[9] = (int)(Temp1[0] >>> 16 & (long)255);
        Temp[10] = (int)(Temp1[0] >>> 8 & (long)255);
        Temp[11] = (int)(Temp1[0] & (long)255);
        Temp[12] = (int)(Temp1[1] >>> 24 & (long)255);
        Temp[13] = (int)(Temp1[1] >>> 16 & (long)255);
        Temp[14] = (int)(Temp1[1] >>> 8 & (long)255);
        Temp[15] = (int)(Temp1[1] & (long)255);
        Sub(A, ExtendKey[0], Temp1, CEA_MPI_LONG);
        Temp[0] = (int)(Temp1[0] >>> 24 & (long)255);
        Temp[1] = (int)(Temp1[0] >>> 16 & (long)255);
        Temp[2] = (int)(Temp1[0] >>> 8 & (long)255);
        Temp[3] = (int)(Temp1[0] & (long)255);
        Temp[4] = (int)(Temp1[1] >>> 24 & (long)255);
        Temp[5] = (int)(Temp1[1] >>> 16 & (long)255);
        Temp[6] = (int)(Temp1[1] >>> 8 & (long)255);
        Temp[7] = (int)(Temp1[1] & (long)255);
        IntToByte(Temp, cipher, 16);
    }

    public void Copy(long source[], long result[], int length)
    {
        for(int i = 0; i < length; i++)
            result[i] = source[i];

    }

    public int Decrypt(byte in_data[], byte out_data[], int in_len, byte key[], int key_len)
    {
        if(in_len < 16)
            return -1;
        byte in_temp[] = new byte[16];
        byte out_temp[] = new byte[16];
        init(key, key_len);
        int remainder = in_len % 16;
        int n = in_len / 16;
        if(remainder == 0)
        {
            for(int i = 0; i < n; i++)
            {
                int k = i * 16;
                for(int j = 0; j < 16; j++)
                {
                    in_temp[j] = in_data[k];
                    k++;
                }

                CEADecrypt(in_temp, out_temp);
                k = i * 16;
                for(int j = 0; j < 16; j++)
                {
                    out_data[k] = out_temp[j];
                    k++;
                }

            }

        } else
        {
            int k;
            for(int i = 0; i < n - 1; i++)
            {
                k = i * 16;
                for(int j = 0; j < 16; j++)
                {
                    in_temp[j] = in_data[k];
                    k++;
                }

                CEADecrypt(in_temp, out_temp);
                k = i * 16;
                for(int j = 0; j < 16; j++)
                {
                    out_data[k] = out_temp[j];
                    k++;
                }

            }

            k = (n - 1) * 16 + remainder;
            for(int j = 0; j < 16; j++)
            {
                in_temp[j] = in_data[k];
                k++;
            }

            CEADecrypt(in_temp, out_temp);
            k = n * 16;
            for(int j = 0; j < remainder; j++)
            {
                out_data[k] = out_temp[j];
                k++;
            }

            k = (n - 1) * 16;
            for(int j = 0; j < remainder; j++)
            {
                in_temp[j] = in_data[k];
                k++;
            }

            for(int j = remainder; j < 16; j++)
                in_temp[j] = out_temp[j];

            CEADecrypt(in_temp, out_temp);
            k = (n - 1) * 16;
            for(int j = 0; j < 16; j++)
            {
                out_data[k] = out_temp[j];
                k++;
            }

        }
        return 0;
    }

    public int Encrypt(byte in_data[], byte out_data[], int in_len, byte key[], int key_len)
    {
        if(in_len < 16)
            return -1;
        byte in_temp[] = new byte[16];
        byte out_temp[] = new byte[16];
        init(key, key_len);
        int remainder = in_len % 16;
        int n = in_len / 16;
        for(int i = 0; i < n; i++)
        {
            int k = i * 16;
            for(int j = 0; j < 16; j++)
            {
                in_temp[j] = in_data[k];
                k++;
            }

            CEAEncrypt(in_temp, out_temp);
            k = i * 16;
            for(int j = 0; j < 16; j++)
            {
                out_data[k] = out_temp[j];
                k++;
            }

        }

        if(remainder != 0)
        {
            int k = n * 16;
            for(int j = 0; j < remainder; j++)
            {
                in_temp[j] = in_data[k];
                k++;
            }

            k = (n - 1) * 16 + remainder;
            for(int j = remainder; j < 16; j++)
            {
                in_temp[j] = out_data[k];
                k++;
            }

            CEAEncrypt(in_temp, out_temp);
            k = (n - 1) * 16 + remainder;
            for(int j = 0; j < 16; j++)
            {
                out_data[k] = out_temp[j];
                k++;
            }

        }
        return 0;
    }

    public void init(byte key[], int keylength)
    {
        long SrcKey[][] = new long[2 * CEA_ROTATE + 4][CEA_MPI_LONG];
        long A[] = new long[CEA_MPI_LONG];
        long B[] = new long[CEA_MPI_LONG];
        long Temp1[] = new long[CEA_MPI_LONG];
        long Temp2[] = new long[CEA_MPI_LONG];
        for(int i = 0; i < CEA_MPI_LONG; i++)
        {
            A[i] = 0L;
            B[i] = 0L;
            Temp1[i] = 0L;
            Temp2[i] = 0L;
        }

        int keycount = (keylength + (CEA_MPI_LONG * 4 - 1)) / (CEA_MPI_LONG * 4);
        int temp[] = new int[keycount * (CEA_MPI_LONG * 4)];
        for(int i = 0; i < keycount * (CEA_MPI_LONG * 4); i++)
            temp[i] = 0;

        ByteToInt(key, temp, keylength);
        int n = 0;
        for(int i = 0; i < keycount; i++)
        {
            for(int j = 0; j < CEA_MPI_LONG; j++)
            {
                SrcKey[i][j] = 0L;
                for(int k = 0; k < 4; k++)
                {
                    SrcKey[i][j] = SrcKey[i][j] << 8 | (long)(temp[n] & 0xff);
                    n++;
                }

            }

        }

        ExtendKey[0][0] = 0xb7e15163L;
        ExtendKey[0][1] = 0x9e3779b9L;
        for(int i = 1; i < 2 * CEA_ROTATE + 4; i++)
            LShift(ExtendKey[i - 1], ExtendKey[i], i, CEA_MPI_LONG);

        for(int i = 0; i < 2 * CEA_ROTATE + 4; i++)
        {
            Add(A, B, Temp1, CEA_MPI_LONG);
            Add(ExtendKey[i], Temp1, Temp2, CEA_MPI_LONG);
            LShift(Temp2, ExtendKey[i], 5, CEA_MPI_LONG);
            Copy(ExtendKey[i], A, CEA_MPI_LONG);
            Add(A, B, Temp1, CEA_MPI_LONG);
            Add(Temp1, SrcKey[i % keycount], Temp2, CEA_MPI_LONG);
            long t = Temp1[1] % (long)64;
            LShift(Temp2, SrcKey[i % keycount], (int)t, CEA_MPI_LONG);
            Copy(SrcKey[i % keycount], B, CEA_MPI_LONG);
        }

    }

    public void IntToByte(int source[], byte result[], int length)
    {
        for(int i = 0; i < length; i++)
            if(source[i] < 128)
                result[i] = (byte)source[i];
            else
                result[i] = (byte)(source[i] - 256);

    }

    public void LShift(long source[], long result[], int count, int length)
    {
        long mid[] = new long[length];
        count %= length * 32;
        int shiftlong = count >>> 5;
        int shiftbit = count & 0x1f;
        int rshiftbit = 32 - shiftbit;
        for(int i = 0; i < length; i++)
        {
            mid[i] = source[shiftlong] << shiftbit & 0xffffffffL;
            if(++shiftlong >= length)
                shiftlong = 0;
            mid[i] = mid[i] | source[shiftlong] >>> rshiftbit & 0xffffffffL;
        }

        for(int i = 0; i < length; i++)
            result[i] = mid[i];

    }

    public static void main(String args[])
    {
//        CEA cea = new CEA();
//        String text = "1234567890123456789012345678901234567890";
//        String TransferKey = "11111111";
//        byte cipher[] = new byte[41];
//        byte plain1[] = new byte[41];
//        int res = 0;
//        byte plain[] = text.getBytes();
//        byte key[] = TransferKey.getBytes();
//        res = cea.Encrypt(plain, cipher, 40, key, 8);
//        res = cea.Decrypt(cipher, plain1, 40, key, 8);
//        String str = new String(plain1);
//        System.out.println("str=".concat(String.valueOf(String.valueOf(str))));
    }

    public void RShift(long source[], long result[], int count, int length)
    {
        count %= length * 32;
        count = length * 32 - count;
        count %= length * 32;
        LShift(source, result, count, length);
    }

    public void Sub(long s1[], long s2[], long result[], int length)
    {
        long b = 0L;
        for(int i = length - 1; i >= 0; i--)
        {
            result[i] = s1[i] - s2[i] - b;
            if(result[i] >= (long)0)
            {
                b = 0L;
            } else
            {
                result[i] = result[i] + 0x100000000L;
                b = 1L;
            }
        }

    }

    public void Xor(long s1[], long s2[], long result[], int length)
    {
        for(int i = 0; i < length; i++)
            result[i] = s1[i] ^ s2[i];

    }
}
