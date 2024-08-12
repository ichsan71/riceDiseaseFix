package com.marqumil.tensorflowlitetest.data

data class Handling(
    val name: String,
    val description: List<String>
)

val handlings = listOf(
    Handling(
        name = "Blast",
        description = listOf(
            "Gunakan varietas tahan.",
            "Lakukan seleksi benih dengan menggunakan larutan garam dengan dosis 50 g/liter air.",
            "Lakukan perlakuan benih (seed treatment) dengan memanfaatkan agens hayati Paenibacillus polymyxa 5-10 mL/liter selama 15 menit atau fungisida sesuai anjuran.",
            "Lakukan pengendalian di pesemaian dengan memanfaatkan agens hayati Paenibacillus polymyxa 5-10 mL/liter atau fungisida sesuai anjuran pada umur 10-15 hari setelah semai (hss).",
            "Lakukan aplikasi agens hayati Paenibacillus polymyxa 5-10 mL/liter air pada umur 14, 28 dan 42 hari setelah tanam (hst) pada sore hari (± pukul 15.00) atau fungisida sesuai anjuran di pertanaman.",
            "Lakukan sanitasi lingkungan disekitar pertanaman.",
            "Lakukan pemupukan berimbang."
        )
    ),
    Handling(
        name = "Hawar Daun Bakteri",
        description = listOf(
            "Gunakan varietas tahan.",
            "Lakukan seleksi benih dengan menggunakan larutan garam dengan dosis 50 g/liter air.",
            "Lakukan perlakuan benih (seed treatment) dengan memanfaatkan agens hayati sesuai anjuran.",
            "Lakukan pengendalian di pesemaian dengan memanfaatkan agens hayati Paenibacillus polymyxa 5-10 mL/liter pada umur 10-15 hss.",
            "Aplikasi agens hayati Paenibacillus polymyxa 5-10 mL/liter air di pertanaman pada umur 14, 28 dan 42 hst pada sore hari (± pukul 15.00).",
            "Lakukan sanitasi lingkungan disekitar pertanaman.",
            "Lakukan pemupukan berimbang."
        )
    ),
    Handling(
        name = "Normal",
        description = listOf(
            "Sanitasi Lingkungan",
            "Pemupukan Berimbang, lakukan pemupukan yang berimbang dengan memperhatikan kebutuhan unsur hara N, P, dan K. Pemupukan yang berimbang akan membantu tanaman tumbuh sehat dan kuat menghadapi serangan penyakit.",
            "Pengelolaan Air yang Tepat",
            "Penanaman Secara Serempak",
            "Pemantauan Rutin"
        )
    ),
    Handling(
        name = "Tungro",
        description = listOf(
            "Menanam Secara Serempak",
            "Menanam bibit sebulan sebelum terjadi puncak kepadatan wereng hijau. Puncak populasi wereng hijau terjadi pada 1,5-2 bulan setelah curah hujan mencapai puncaknya.",
            "Menanam Varietas Tahan, beberapa varietas tahan tungro antara lain Tukad Petanu, Tukad Unda, Tukad Balian, Kalimas, Bondoyudo",
            "Eradikasi atau memusnahkan tanaman sakit merupakan tindakan yang harus dilakukan untuk menghilangkan sumber inokulum.",
            "Penggunaan Pupuk N yang Tepat",
            "Sawah Jangan Dikeringkan"
        )
    ),
)



