// See:
// - https://authjs.dev/getting-started/adapters/prisma#prismaadapter
// - https://github.com/nextauthjs/next-auth/blob/next-auth%405.0.0-beta.29/docs/pages/getting-started/adapters/prisma.mdx

import { PrismaClient } from "@prisma/client"

const globalForPrisma = globalThis as unknown as { prisma: PrismaClient }

export const prisma = globalForPrisma.prisma || new PrismaClient()

if (process.env.NODE_ENV !== "production") globalForPrisma.prisma = prisma
