# Contributing to Strigops Account Service

We welcome contributions from the community! This project follows standard open-source practices. Please read this guide before contributing.

## Development Setup

1. **Fork the repository** on GitHub
2. **Clone your fork:**
   ```bash
   git clone https://github.com/your-username/strigops-account.git
   cd strigops-account
   ```
3. **Add upstream remote:**
   ```bash
   git remote add upstream https://github.com/original-repo/strigops-account.git
   ```
4. **Create a feature branch:**
   ```bash
   git checkout -b feature/your-feature-name
   ```
5. **Set up development environment:**
   ```bash
   make install-deps
   make docker-setup
   ```
6. **Make your changes** and ensure tests pass:
   ```bash
   make test
   ```

## Branching Strategy

- **main**: Production-ready code
- **develop**: Latest development changes
- **feature/***: New features (e.g., `feature/add-oauth2-provider`)
- **bugfix/***: Bug fixes (e.g., `bugfix/fix-otp-validation`)
- **hotfix/***: Critical fixes for production

## Code Guidelines

- **Java**: Follow standard Java naming conventions
- **Commits**: Use clear, descriptive commit messages
- **Tests**: Write unit tests for new features
- **Documentation**: Update README and code comments as needed

## Pull Request Process

1. **Ensure your branch is up-to-date:**
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```
2. **Run full test suite:**
   ```bash
   make run-with-test
   ```
3. **Push your branch:**
   ```bash
   git push origin feature/your-feature-name
   ```
4. **Create a Pull Request** on GitHub:
   - Provide a clear title and description
   - Reference any related issues
   - Ensure CI checks pass
5. **Wait for review** and address any feedback

## Testing

- Run `make test` for unit tests
- Run `make run-with-test` for integration tests
- Ensure all tests pass before submitting PR

## Code of Conduct

Please be respectful and inclusive. Harassment or discriminatory behavior will not be tolerated.

## Issues

- **Bug reports**: Use the bug report template
- **Feature requests**: Use the feature request template
- **Questions**: Check existing issues first, then create a new one

## License

By contributing, you agree that your contributions will be licensed under the same license as the project (GNU General Public License v3.0).