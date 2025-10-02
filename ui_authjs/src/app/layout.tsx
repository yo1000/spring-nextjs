import React from "react";
import {Noto_Color_Emoji, Noto_Sans, Noto_Sans_JP} from "next/font/google";
import "./globals.css";
import {SessionProvider} from "next-auth/react";
import Navigator from "@/components/Navigator";

const noTofuEmoji = Noto_Color_Emoji({
  weight: "400",
  subsets: ["emoji"],
  display: "swap",
});
const noTofuJp = Noto_Sans_JP({
  subsets: ["latin"],
});
const noTofu = Noto_Sans({
  subsets: ["latin"],
});

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className={`h-full bg-neutral-100`}>
    <body className={`h-full ${noTofuEmoji.className} ${noTofuJp.className} ${noTofu.className} antialiased`}>
    <SessionProvider>
      <div className="min-h-full">
        <Navigator/>
        {children}
      </div>
    </SessionProvider>
    </body>
    </html>
  );
}
