package com.marqumil.tensorflowlitetest.data

data class Handling(
    val name: String,
    val description: String
)

val handlings = listOf(
    Handling(
        name = "Blast",
        description = "-\tGunakan varietas tahan. \n" +
                "-\tLakukan seleksi benih dengan menggunakan larutan garam dengan dosis 50 g/liter air. \n" +
                "-\tLakukan perlakuan benih (seed treatment) dengan memanfaatkan agens hayati Paenibacillus polymyxa 5-10 mL/liter selama 15 menit atau fungisida sesuai anjuran. \n" +
                "-\tLakukan pengendalian di pesemaian dengan memanfaatkan agens hayati Paenibacillus polymyxa 5-10 mL/liter atau fungisida sesuai anjuran pada umur 10-15 hari setelah semai (hss). \n" +
                "-\tLakukan aplikasi agens hayati Paenibacillus polymyxa 5-10 mL/liter air pada umur 14, 28 dan 42 hari setelah Buku Saku Penyakit Padi | 9 tanam (hst) pada sore hari (± pukul 15.00) atau fungisida sesuai anjuran di pertanaman. \n" +
                "-\tLakukan sanitasi lingkungan disekitar pertanaman. \n" +
                "-\tLakukan pemupukan berimbang.\n"
    ),
    Handling(
        name = "Hawar Daun Bakteri",
        description = "-\tGunakan varietas tahan. \n" +
                "-\tLakukan seleksi benih dengan menggunakan larutan garam dengan dosis 50 g/liter air. \n" +
                "-\tLakukan perlakuan benih (seed treatment) dengan memanfaatkan agens hayati sesuai anjuran. Buku Saku Penyakit Padi\n" +
                "-\tLakukan pengendalian di pesemaian dengan memanfaatkan agens hayati Paenibacillus polymyxa 5-10 mL/liter pada umur 10-15 hss. \n" +
                "-\tAplikasi agens hayati Paenibacillus polymyxa 5-10 mL/liter air di pertanaman pada umur 14, 28 dan 42 hst pada sore hari (± pukul 15.00). \n" +
                "-\tLakukan sanitasi lingkungan disekitar pertanaman. \n" +
                "-\tLakukan pemupukan berimbang.\n"
    ),
    Handling(
        name = "Normal",
        description = "-\tSanitasi Lingkungan\n" +
                "-\tPemupukan Berimbang, lakukan pemupukan yang berimbang dengan memperhatikan kebutuhan unsur hara N, P, dan K. Pemupukan yang berimbang akan membantu tanaman tumbuh sehat dan kuat menghadapi serangan penyakit.\n" +
                "-\tPengelolaan Air yang Tepat\n" +
                "-\tPenanaman Secara Serempak\n" +
                "-\tPemantauan Rutin\n"
    ),
    Handling(
        name = "Tungro",
        description = "-\tMenanam Secara Serempak\n" +
                "-\tMenaman bibit sebulan sebelum terjadi puncak kepadatan wereng hijau. Puncak populasi wereng hijau terjadi pada 1,5- 2 bulan setelah curah hujan mencapai puncaknya.\n" +
                "-\tMenanam Varietas Tahan, beberapa varietas tahan tungro antara lain Tukad Petanu, Tukad Unda, Tukad Balian, Kalimas, Bondoyudo\n" +
                "-\tEradikasi atau memusnahkan tanaman Sakit merupakan tindakan yang harus dilakukan untuk menghilangkan sumber inokulum.\n" +
                "-\tPenggunaan Pupuk N yang Tepat\n" +
                "-\tSawah Jangan Dikeringkan\n"
    ),
)


