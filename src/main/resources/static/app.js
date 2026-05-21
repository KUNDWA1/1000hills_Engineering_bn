const productGrid = document.getElementById('productGrid');

function createProductCard(product) {
  const card = document.createElement('article');
  card.className = 'product-card';

  const image = document.createElement('img');
  image.src = product.imageUrl || 'https://via.placeholder.com/600x450?text=Product';
  image.alt = product.title || 'Engineering product';

  const body = document.createElement('div');
  body.className = 'product-card-body';

  const title = document.createElement('h3');
  title.textContent = product.title || 'Unnamed product';

  const meta = document.createElement('div');
  meta.className = 'meta';
  meta.textContent = product.category ? product.category : 'Engineering materials';

  const price = document.createElement('div');
  price.className = 'price';
  price.textContent = `${product.currency || 'USD'} ${product.price?.toFixed(2) || '0.00'}`;

  const description = document.createElement('p');
  description.className = 'description';
  description.textContent = product.description || 'Reliable product for construction and engineering procurement.';

  const stock = document.createElement('div');
  stock.className = 'meta';
  stock.textContent = `Stock: ${product.stockQuantity ?? 'N/A'}`;

  body.append(title, meta, price, stock, description);
  card.append(image, body);
  return card;
}

function renderProducts(products) {
  productGrid.innerHTML = '';

  if (!products || products.length === 0) {
    const empty = document.createElement('div');
    empty.className = 'product-loading';
    empty.textContent = 'No featured products are available right now.';
    productGrid.append(empty);
    return;
  }

  products.slice(0, 6).forEach(product => {
    productGrid.append(createProductCard(product));
  });
}

async function fetchFeaturedProducts() {
  try {
    const response = await fetch('/api/products/featured');
    if (!response.ok) {
      throw new Error(`API error: ${response.status}`);
    }
    const products = await response.json();
    renderProducts(products);
  } catch (error) {
    productGrid.innerHTML = '';
    const errorMessage = document.createElement('div');
    errorMessage.className = 'product-loading';
    errorMessage.textContent = 'Unable to load featured products. Please ensure the backend is running.';
    productGrid.append(errorMessage);
    console.error('Failed to fetch featured products:', error);
  }
}

fetchFeaturedProducts();
