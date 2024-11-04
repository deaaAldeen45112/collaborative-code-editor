// src/assets/js/swiper-config.js
const swiperConfig = {
  loop: true,
  speed: 600,
  autoplay: {
    delay: 5000
  },
  slidesPerView: 'auto',
  pagination: {
    el: '.swiper-pagination',
    type: 'bullets',
    clickable: true
  },
  breakpoints: {
    320: {
      slidesPerView: 1,
      spaceBetween: 40
    },
    1200: {
      slidesPerView: 2,
      spaceBetween: 20
    }
  }
};

// If you are using ES6 module system
export default swiperConfig;
