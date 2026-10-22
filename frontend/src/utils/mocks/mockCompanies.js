export const mockCompanies = [
  {
    username: 'techstore@unir.net',
    password: '123456',
    user: {
      userId: 1,
      company: {
        name: 'TechStore',
        sector: 'Tecnología',
        email: 'info@techstore.com',
        phone: '+34 941 100 100',
        address: 'Calle Gran Vía 12, Logroño',
        cif: 'B12345678',
        employeeCount: 24,
        founded: '2018'
      },
      recentOrders: [
        {
          id: 'TS-1001',
          date: '2026-05-01',
          status: 'entregado',
          items: [
            { name: 'Clean Code', quantity: 2, price: 39.95 },
            { name: 'Pro Git', quantity: 1, price: 0 }
          ],
          total: 79.9
        }
      ]
    }
  },
  {
    username: 'digitaloffice@unir.net',
    password: 'password123',
    user: {
      userId: 2,
      company: {
        name: 'Digital Office',
        sector: 'Ofimática',
        email: 'contacto@digitaloffice.com',
        phone: '+34 941 200 200',
        address: 'Avenida de la Paz 45, Logroño',
        cif: 'B87654321',
        employeeCount: 12,
        founded: '2020'
      },
      recentOrders: [
        {
          id: 'DO-2044',
          date: '2026-04-26',
          status: 'en_proceso',
          items: [
            { name: 'React Design Patterns and Best Practices', quantity: 1, price: 27.5 }
          ],
          total: 27.5
        }
      ]
    }
  },
  {
    username: 'innovacorp@unir.net',
    password: 'admin2025',
    user: {
      userId: 3,
      company: {
        name: 'InnovaCorp',
        sector: 'Innovación',
        email: 'hello@innovacorp.com',
        phone: '+34 941 300 300',
        address: 'Calle Chile 8, Logroño',
        cif: 'B11223344',
        employeeCount: 51,
        founded: '2016'
      },
      recentOrders: [
        {
          id: 'IC-7788',
          date: '2026-05-03',
          status: 'pendiente',
          items: [
            { name: 'Software Engineering at Google', quantity: 3, price: 44 }
          ],
          total: 132
        }
      ]
    }
  }
]
